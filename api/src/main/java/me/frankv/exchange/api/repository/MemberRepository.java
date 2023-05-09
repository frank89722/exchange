package me.frankv.exchange.api.repository;

import me.frankv.exchange.api.entity.MemberEntity;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, ObjectId> {
    Optional<MemberEntity> findAllById(ObjectId id);
}
