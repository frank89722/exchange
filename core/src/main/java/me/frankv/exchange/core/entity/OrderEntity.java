package me.frankv.exchange.core.entity;

import com.mongodb.lang.NonNull;
import lombok.Builder;
import lombok.Data;
import me.frankv.exchange.common.model.Order;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Builder
@Document("order")
@CompoundIndex(
        name = "price_id",
        def = "{ 'direction': 1, 'price': -1, 'id': -1 }"
)
public class OrderEntity implements Order {

    @Id
    private ObjectId id;

    private BigDecimal price;
    @NonNull
    private BigDecimal amount;

    @Field(name = "member_id")
    private ObjectId memberId;

    @NonNull
    private Direction direction;

}
