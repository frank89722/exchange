package me.frankv.core.dto;

import me.frankv.core.entity.Order;

import java.math.BigDecimal;

public record OrderDTO(
        String id,
        BigDecimal price,
        BigDecimal amount,
        Order.Type type

) { }
