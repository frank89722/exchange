package me.frankv.exchange.common.model;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A transaction is a record of a wallet being deposited or withdrawn.
 */
public interface Transaction extends Serializable {

    ObjectId getId();

    void setId(ObjectId id);

    ObjectId getMemberId();

    void setMemberId(ObjectId memberId);

    String getToken();

    void setToken(String token);

    BigDecimal getAmount();

    void setAmount(BigDecimal amount);

    BigDecimal getPrice();

    void setPrice(BigDecimal price);
}
