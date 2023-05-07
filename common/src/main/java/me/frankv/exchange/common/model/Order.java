package me.frankv.exchange.common.model;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.math.BigDecimal;

public interface Order extends Serializable {
    ObjectId getId();
    void setId(ObjectId id);

    BigDecimal getPrice();
    void setPrice(BigDecimal price);

    BigDecimal getAmount();
    void setAmount(BigDecimal amount);

    ObjectId getMemberId();
    void setMemberId(ObjectId memberId);

    Type getType();
    void setType(Type type);


    @ToString(includeFieldNames = false)
    @RequiredArgsConstructor
    enum Type {
        SELL("sell"), BUY("buy");

        public final String text;
    }
}
