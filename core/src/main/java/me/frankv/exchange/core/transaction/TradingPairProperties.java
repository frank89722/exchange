package me.frankv.exchange.core.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradingPairProperties {
    private String name;
    private String takeToken;
    private String giveToken;
}
