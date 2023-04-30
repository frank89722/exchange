package me.frankv.core.repository;

import me.frankv.core.entity.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    Set<Order> getAllByType(Order.Type type);
}
