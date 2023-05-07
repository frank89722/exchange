package me.frankv.exchange.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import me.frankv.exchange.common.model.Transaction;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Data
@Entity
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
