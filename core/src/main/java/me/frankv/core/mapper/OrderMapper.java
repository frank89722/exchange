package me.frankv.core.mapper;

import me.frankv.core.dto.OrderDTO;
import me.frankv.core.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements EntityMapper<Order, OrderDTO> {

    @Override
    public Order mapToEntity(OrderDTO dto) {
        return null;
    }

    @Override
    public OrderDTO mapToDto(Order entity) {
        return null;
    }
}
