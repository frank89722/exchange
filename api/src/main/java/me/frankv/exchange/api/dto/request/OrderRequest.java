package me.frankv.exchange.api.dto.request;

import lombok.NonNull;

public record OrderRequest(
        @NonNull String memberId,
        @NonNull String amount,
        @NonNull String price,
        @NonNull String type,
        @NonNull String tradingPairName
){}
