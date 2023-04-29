package me.frankv.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

//    @Id
    private String id;

    private BigDecimal price;
    private BigDecimal amount;

    private Type type;

    private LocalDateTime localDateTime;

    public enum Type {
        SELL, BUY
    }
}
