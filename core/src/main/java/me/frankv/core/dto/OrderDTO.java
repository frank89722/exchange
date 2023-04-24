package me.frankv.core.dto;

import me.frankv.core.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(
        String id,
        BigDecimal price,
        BigDecimal amount,
        Order.Type type,

        LocalDateTime localDateTime
) { }
