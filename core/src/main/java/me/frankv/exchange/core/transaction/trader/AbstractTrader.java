package me.frankv.exchange.core.transaction.trader;

import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.common.model.Order;
import me.frankv.exchange.common.util.OrderUtils;
import me.frankv.exchange.core.entity.OrderEntity;
import me.frankv.exchange.core.repository.OrderRepository;
import me.frankv.exchange.core.transaction.TradingPair;

import java.math.BigDecimal;
import java.util.TreeMap;
import java.util.TreeSet;

@Slf4j
public abstract class AbstractTrader implements Trader {

    protected TradingPair tradingPair;
    protected TreeMap<BigDecimal, TreeSet<Order>> sellOrders;
    protected TreeMap<BigDecimal, TreeSet<Order>> buyOrders;
    protected OrderRepository repository;

    @Override
    public void init(TreeMap<BigDecimal, TreeSet<Order>> sellOrders, TreeMap<BigDecimal, TreeSet<Order>> buyOrders) {
        this.sellOrders = sellOrders;
        this.buyOrders = buyOrders;
    }

    protected void writeOrder(Order order) {
        repository.save((OrderEntity) order);
        addOrderToMap(order);
    }

    protected void wipeOrder(Order order) {
        repository.deleteById(order.getId());
        removeOrderFromMap(order);
    }

    protected void removeOrderFromMap(Order order) {
        (order.getDirection() == Order.Direction.SELL ? sellOrders : buyOrders)
                .get(order.getPrice()).remove(order);
    }

    protected void addOrderToMap(Order order) {
        var orders = (order.getDirection() == Order.Direction.SELL ? sellOrders : buyOrders);
        var set = orders.computeIfAbsent(order.getPrice(), k -> OrderUtils.getOrderTreeSet());
        set.add(order);
    }
}
