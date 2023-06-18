package me.frankv.exchange.core.transaction.trader;

import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.common.model.Order;
import me.frankv.exchange.core.entity.OrderEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class LimitTrader extends AbstractTrader {

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

            if (order.getAmount().compareTo(existedOrder.getAmount()) >= 0) {
                order.setAmount(order.getAmount().subtract(existedOrder.getAmount()));
                existedOrder.setAmount(BigDecimal.ZERO);
                wipeOrder(existedOrder);
                tradableIndex++;
            } else {
                existedOrder.setAmount(existedOrder.getAmount().subtract(order.getAmount()));
                repository.save((OrderEntity) existedOrder);
                order.setAmount(BigDecimal.ZERO);
            }

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

}
