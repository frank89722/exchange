package me.frankv.exchange.core.transaction.trader;

import me.frankv.exchange.core.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.List;

public interface Trader {

    /**
     *
     * @param tradables A list of tradable orders
     * @param orderEntity The order to trade
     * @return latest transaction price
     */
    BigDecimal trade(List<OrderEntity> tradables, OrderEntity orderEntity);
}
