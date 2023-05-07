package me.frankv.exchange.core.config;

import me.frankv.exchange.core.repository.OrderRepository;
import me.frankv.exchange.core.transaction.TradingPair;
import me.frankv.exchange.core.transaction.TradingPairImpl;
import me.frankv.exchange.core.transaction.TradingPairProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TradingPairConfig {

    @Bean
    public TradingPair testTradingPair(OrderRepository orderRepository) {
        var testPairProperties = new TradingPairProperties("test");
        var tradingPair = new TradingPairImpl(testPairProperties, orderRepository);
        tradingPair.init();
        return tradingPair;
    }
}
