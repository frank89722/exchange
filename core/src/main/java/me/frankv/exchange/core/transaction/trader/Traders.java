package me.frankv.exchange.core.transaction.trader;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Traders {
    LIMIT(new LimitTrader());

    @Getter
    private final Trader trader;
}
