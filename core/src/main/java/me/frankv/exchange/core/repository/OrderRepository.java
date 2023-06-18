package me.frankv.exchange.core.repository;

import me.frankv.exchange.common.model.Order;
import me.frankv.exchange.core.entity.OrderEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, ObjectId> {
    Set<OrderEntity> getAllByType(Order.Direction direction);
}
