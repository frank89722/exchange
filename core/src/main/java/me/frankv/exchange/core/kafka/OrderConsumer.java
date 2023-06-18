package me.frankv.exchange.core.kafka;

import lombok.NoArgsConstructor;
import me.frankv.exchange.common.dto.OrderDto;
import me.frankv.exchange.common.model.Order;
import me.frankv.exchange.core.entity.OrderEntity;
import me.frankv.exchange.core.transaction.TradingPair;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class OrderConsumer {
    private Map<String, TradingPair> tradingPairs = null;

    @Autowired
    public void setupTradingPairMap(List<TradingPair> tradingPairSet) {
        tradingPairs = tradingPairSet.stream()
                .collect(Collectors.toUnmodifiableMap(o -> o.getProperties().getName(), o -> o));
    }

    @KafkaListener(
            topics = "topic-order",
            groupId = "trader",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload OrderDto orderDto) {
        var pair = tradingPairs.get("test");

        var order = OrderEntity.builder()
                .id(new ObjectId())
                .amount(new BigDecimal(orderDto.amount()))
                .price(new BigDecimal(orderDto.price()))
                .direction(switch (orderDto.type()) {
                    case "buy" -> Order.Direction.BUY;
                    case "sell" -> Order.Direction.SELL;
                    default -> throw new IllegalArgumentException(
                            String.format("type '%s' not found", orderDto.type()));
                })
                .build();

        pair.addOrder(order);
    }
}

