package me.frankv.exchange.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.frankv.exchange.common.model.Transaction;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "transaction",
        indexes = @Index(columnList = "token")
)
public class TransactionEntity implements Transaction {

    @Id
    private ObjectId id;
    @Column(name = "member_id")
    private ObjectId memberId;
    private String token;
    private BigDecimal amount;
    private BigDecimal price;
}
