package me.frankv.exchange.core.transaction.trader;

import me.frankv.exchange.common.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public interface Trader {

    void init(TreeMap<BigDecimal, TreeSet<Order>> sellOrders, TreeMap<BigDecimal, TreeSet<Order>> buyOrders);

    /**
     * Start a trade process.
     * @param orderEntity The order to trade
     * @return latest transaction price
     */
    BigDecimal trade(Order orderEntity);

    TraderType getType();
    enum TraderType {
        LIMIT
    }
}
