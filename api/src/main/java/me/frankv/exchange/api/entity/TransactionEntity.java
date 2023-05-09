package me.frankv.exchange.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.frankv.exchange.common.model.Transaction;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "transaction",
        indexes = @Index(columnList = "trading_pair_name")
)
public class TransactionEntity implements Transaction {

    @Id
    private ObjectId id;
    @Column(name = "member_id")
    private ObjectId memberId;
    @Column(name = "trading_pair_name")
    private String tradingPairName;
    private BigDecimal amount;
    private BigDecimal price;
}
