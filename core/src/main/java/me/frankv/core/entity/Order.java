package me.frankv.core.entity;

import com.mongodb.lang.NonNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.math.BigDecimal;

@Data
@Builder
@CompoundIndex(name = "id_price", def = "{ 'id': -1, 'price': -1 }")
public class Order {

    @Id
    private ObjectId id;

    @NonNull
    private BigDecimal price;
    @NonNull
    private BigDecimal amount;

    private ObjectId memberId;

    @NonNull
    private Type type;


    @ToString(includeFieldNames = false)
    @RequiredArgsConstructor
    public enum Type {
        SELL("sell"), BUY("buy");

        public final String text;
    }
}
