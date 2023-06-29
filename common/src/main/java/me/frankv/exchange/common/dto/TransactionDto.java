package me.frankv.exchange.common.dto;

public record TransactionDto (
        String id,
        String memberId,
        String token,
        String amount,
        String price
){}
