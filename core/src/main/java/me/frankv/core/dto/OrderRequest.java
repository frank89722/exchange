package me.frankv.core.dto;

public record OrderRequest(
        String price,
        String amount,
        String type

) { }
