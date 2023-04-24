package me.frankv.core.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.frankv.core.dto.OrderDTO;
import me.frankv.core.entity.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
public class TradingPairImpl implements TradingPair {

    private final NavigableMap<BigDecimal, List<Order>> sellOrders;
    private final NavigableMap<BigDecimal, List<Order>> buyOrders;

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @Getter
    private BigDecimal latestPrice;

    @Override
    public synchronized void addOrder(Order order) {
        if (order.getType() == Order.Type.SELL) {
            addSellOrder(order);
            trade();
        } else if (order.getType() == Order.Type.BUY) {
            addBuyOrder(order);
            trade();
        }
    }

    @Override
    public synchronized void trade() {
        var lowestBuyOrderEntry = buyOrders.firstEntry();

        List<Order> buyable = sellOrders.entrySet().stream()
                .filter(o -> o.getKey().compareTo(lowestBuyOrderEntry.getKey()) < 1)
                .flatMap(o -> o.getValue().stream())
                .toList();

        int buyableIndex = 0;
        BigDecimal latest = getLatestPrice();
        for (Order order : lowestBuyOrderEntry.getValue()) {
            while (buyableIndex < buyable.size()) {
                Order sellOrder = buyable.get(buyableIndex);

                if (order.getAmount().compareTo(sellOrder.getAmount()) >= 0) {
                    order.setAmount(order.getAmount().subtract(sellOrder.getAmount()));
                    sellOrder.setAmount(BigDecimal.ZERO);
                } else {
                    sellOrder.setAmount(sellOrder.getAmount().subtract(order.getAmount()));
                    order.setAmount(BigDecimal.ZERO);
                }

                latest = sellOrder.getPrice();

                if (sellOrder.getAmount().equals(BigDecimal.ZERO)) {
                    buyableIndex++;
                }
            }
        }

        latestPrice = latest;

    }

    private void addSellOrder(Order order) {
        addOrder(order, sellOrders);
    }

    private void addBuyOrder(Order order) {
        addOrder(order, buyOrders);
    }

    private void addOrder(Order order, NavigableMap<BigDecimal, List<Order>> orders) {
        List<Order> list = orders.computeIfAbsent(order.getPrice(), k -> new ArrayList<>());
        list.add(order);
    }



}
