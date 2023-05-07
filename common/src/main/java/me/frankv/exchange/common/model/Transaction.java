package me.frankv.exchange.common.model;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.math.BigDecimal;

public interface Transaction extends Serializable {

    ObjectId getId();

    void setId(ObjectId id);

    ObjectId getMemberId();

    void setMemberId(ObjectId memberId);

    String getTradingPairName();

    void setTradingPairName(String tradingPairName);

    BigDecimal getAmount();

    void setAmount(BigDecimal amount);

    BigDecimal getPrice();

    void setPrice(BigDecimal price);
}
