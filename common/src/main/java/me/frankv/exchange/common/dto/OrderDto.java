package me.frankv.exchange.common.dto;

import lombok.Builder;

@Builder
public record OrderDto(
        String id,
        String price,
        String amount,
        String type

) { }
