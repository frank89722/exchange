package me.frankv.exchange.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.frankv.exchange.common.model.Wallet;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "wallet"
)
public class WalletEntity implements Wallet {

    @Id
    private ObjectId id;
    @Column(nullable = false)
    private String tokenName;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(name = "member_id", nullable = false)
    private ObjectId memberId;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "member_id",
//            referencedColumnName = "id",
//            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)
//    )
//    private MemberEntity member;

}
