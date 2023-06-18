package me.frankv.exchange.core.transaction;

import me.frankv.exchange.common.model.Order;
import me.frankv.exchange.core.transaction.trader.Trader;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.List;

public interface TradingPair {

    void init(List<Trader> traders);
    void addOrder(Order order);
    void removeOrder(Order order);
    void removeOrder(String orderId);
    void removeOrder(ObjectId orderId);
    void removeOrderByMemberId(String memberId);

    BigDecimal getLatestPrice();
    boolean isReady();

    TradingPairProperties getProperties();
}
