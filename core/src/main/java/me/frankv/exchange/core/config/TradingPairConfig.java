package me.frankv.exchange.core.config;

import me.frankv.exchange.core.repository.OrderRepository;
import me.frankv.exchange.core.transaction.TradingPair;
import me.frankv.exchange.core.transaction.TradingPairImpl;
import me.frankv.exchange.core.transaction.TradingPairProperties;
import me.frankv.exchange.core.transaction.trader.Trader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class TradingPairConfig {

    @Bean
    public TradingPair testTradingPair(OrderRepository orderRepository, Set<Trader> traders) {
        var testPairProperties = new TradingPairProperties("test", "USDT", "BTC");
        var tradingPair = new TradingPairImpl(testPairProperties, orderRepository);
        tradingPair.init(traders);
        return tradingPair;
    }
}
