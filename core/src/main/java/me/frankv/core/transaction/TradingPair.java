package me.frankv.core.transaction;

import me.frankv.core.entity.Order;

import java.math.BigDecimal;

public interface TradingPair {

    void addOrder(Order order);

    BigDecimal getLatestPrice();
    boolean isReady();
}
