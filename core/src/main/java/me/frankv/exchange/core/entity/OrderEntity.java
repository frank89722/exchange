package me.frankv.exchange.core.entity;

import lombok.*;
import me.frankv.exchange.common.model.Order;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Builder
@CompoundIndex(name = "price_id", def = "{ 'type': 1, 'price': -1, 'id': -1 }")
public class OrderEntity implements Order {

    @Id
    private ObjectId id;

    @lombok.NonNull
    private BigDecimal price;
    @lombok.NonNull
    private BigDecimal amount;

    @Field(name = "member_id")
    private ObjectId memberId;

    @NonNull
    private Type type;


}
