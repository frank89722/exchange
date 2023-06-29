package me.frankv.exchange.core.transaction.trader;

import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.common.dto.TransactionDto;
import me.frankv.exchange.common.model.Order;
import me.frankv.exchange.core.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LimitTrader extends AbstractTrader {
    private KafkaTemplate<String, TransactionDto> kafkaTemplate;

    @Override
    public BigDecimal trade(Order order) {
        var tradable = getTradableOrders(order);
        if (tradable.size() == 0) {
            writeOrder(order);
            return tradingPair.getLatestPrice();
        }

        int tradableIndex = 0;
        int tradeCount = 0;
        var latest = tradingPair.getLatestPrice();

        while (tradableIndex < tradable.size() && !order.getAmount().equals(BigDecimal.ZERO)) {
            var existedOrder = tradable.get(tradableIndex);
            var amount = BigDecimal.ZERO;

            if (order.getAmount().compareTo(existedOrder.getAmount()) >= 0) {
                amount = order.getAmount().subtract(existedOrder.getAmount());
                order.setAmount(amount);
                existedOrder.setAmount(BigDecimal.ZERO);
                wipeOrder(existedOrder);
                tradableIndex++;
            } else {
                amount = existedOrder.getAmount().subtract(order.getAmount());
                existedOrder.setAmount(amount);
                repository.save((OrderEntity) existedOrder);
                order.setAmount(BigDecimal.ZERO);
            }

            //TODO: fucking ugly = =
            kafkaTemplate.send(
                    "topic-transactions",
                    new TransactionDto(
                            null,
                            order.getMemberId().toString(),
                            order.getDirection() == Order.Direction.BUY
                                    ? tradingPair.getProperties().getGiveToken()
                                    : tradingPair.getProperties().getTakeToken(),
                            amount.toString(),
                            existedOrder.getPrice().toString()
                    ));
            tradeCount++;
            latest = existedOrder.getPrice();
        }

        if (!order.getAmount().equals(BigDecimal.ZERO)) {
            writeOrder(order);
        }

        log.info(String.format("Trade count: %d", tradeCount));
        return latest;
    }

    @Override
    public TraderType getType() {
        return TraderType.LIMIT;
    }

    private List<Order> getTradableOrders(Order order) {
        return (order.getDirection() == Order.Direction.SELL ? buyOrders.descendingMap() : sellOrders)
                .entrySet().stream()
                .filter(o -> {
                    if (order.getDirection() == Order.Direction.BUY) {
                        return o.getKey().compareTo(order.getPrice()) < 1;
                    } else {
                        return o.getKey().compareTo(order.getPrice()) > -1;
                    }
                })
                .flatMap(o -> o.getValue().stream())
                .toList();
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, TransactionDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
