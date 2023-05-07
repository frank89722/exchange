package me.frankv.exchange.core.transaction;

import me.frankv.exchange.common.model.Order;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

public interface TradingPair {

    void init();
    void addOrder(Order order);
    void removeOrder(Order order);
    void removeOrder(String orderId);
    void removeOrder(ObjectId orderId);
    void removeOrderByMemberId(String memberId);

    BigDecimal getLatestPrice();
    boolean isReady();

    TradingPairProperties getProperties();
}
