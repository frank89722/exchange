package me.frankv.exchange.core.transaction.controller;

import jakarta.annotation.PostConstruct;
import me.frankv.exchange.core.transaction.TradingPair;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/latest_price")
public class LatestPriceController {
    private Map<String, TradingPair> tradingPairMap;

    public LatestPriceController(Set<TradingPair> tradingPairs) {
        tradingPairMap = tradingPairs.stream()
                .collect(Collectors.toUnmodifiableMap(t -> t.getProperties().getName(), o -> o));
    }

    @GetMapping("/{tradingPair}")
    public String getLatestPrice(@PathVariable String tradingPair) {
        return Optional.ofNullable(tradingPairMap.get(tradingPair))
                .map(TradingPair::getLatestPrice)
                .orElse(BigDecimal.ZERO)
                .toString();
    }
}
