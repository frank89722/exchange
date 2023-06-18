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

    Direction getDirection();
    void setDirection(Direction direction);


    @ToString(includeFieldNames = false)
    @RequiredArgsConstructor
    enum Direction {
        SELL("sell"), BUY("buy");

        public final String text;
    }
}
