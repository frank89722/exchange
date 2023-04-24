package me.frankv.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private String id;

    private BigDecimal price;
    private BigDecimal amount;

    private Type type;

    private LocalDateTime localDateTime;

    public enum Type {
        SELL, BUY
    }
}
