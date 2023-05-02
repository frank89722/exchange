package me.frankv.core.config;

import me.frankv.core.repository.OrderRepository;
import me.frankv.core.transaction.TradingPair;
import me.frankv.core.transaction.TradingPairImpl;
import me.frankv.core.transaction.TradingPairProperties;
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
