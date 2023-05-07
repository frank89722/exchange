package me.frankv.exchange.common.dto;

public record OrderRequest(
        String price,
        String amount,
        String type

) { }
