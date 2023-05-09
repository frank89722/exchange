package me.frankv.exchange.api.repository;

import me.frankv.exchange.api.entity.WalletEntity;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, ObjectId> {
    Set<WalletEntity> findAllByMemberId(ObjectId memberId);
    Optional<WalletEntity> findAllByMemberIdAndTokenName(ObjectId objectId, String tokenName);

}
