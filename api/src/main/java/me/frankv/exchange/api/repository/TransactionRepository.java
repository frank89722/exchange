package me.frankv.exchange.api.repository;

import me.frankv.exchange.api.entity.TransactionEntity;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, ObjectId> {
    Set<TransactionEntity> findAllByMemberId(ObjectId memberId);
    Set<TransactionEntity> findAllByMemberIdAndTradingPairName(ObjectId memberId, String tradingPairName);
    Optional<TransactionEntity> findAllById(ObjectId id);

}
