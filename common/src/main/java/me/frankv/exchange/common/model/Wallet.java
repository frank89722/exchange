package me.frankv.exchange.common.model;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.math.BigDecimal;

public interface Wallet extends Serializable {
    ObjectId getId();
    void setId(ObjectId id);

    String getTokenName();
    void setTokenName(String tokenName);
    BigDecimal getAmount();
    void setAmount(BigDecimal amount);
    ObjectId getMemberId();
    void setMemberId(ObjectId memberId);
}
