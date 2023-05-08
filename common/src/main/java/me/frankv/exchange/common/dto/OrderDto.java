package me.frankv.exchange.common.dto;

public record OrderDto(
        String id,
        String price,
        String amount,
        String type

) { }
