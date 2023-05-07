package me.frankv.exchange.core.kafka;

import lombok.NoArgsConstructor;
import me.frankv.exchange.common.dto.OrderRequest;
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
    public void listener(@Payload OrderRequest orderRequest) {
        var pair = tradingPairs.get("test");

        var order = OrderEntity.builder()
                .id(new ObjectId())
                .amount(new BigDecimal(orderRequest.amount()))
                .price(new BigDecimal(orderRequest.price()))
                .type(switch (orderRequest.type()) {
                    case "buy" -> OrderEntity.Type.BUY;
                    case "sell" -> OrderEntity.Type.SELL;
                    default -> throw new IllegalArgumentException(
                            String.format("type '%s' not found", orderRequest.type()));
                })
                .build();

        pair.addOrder(order);
    }
}

