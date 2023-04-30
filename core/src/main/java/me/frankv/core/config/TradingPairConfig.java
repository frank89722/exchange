package me.frankv.core.config;

import me.frankv.core.entity.Order;
import me.frankv.core.repository.OrderRepository;
import me.frankv.core.transaction.TradingPair;
import me.frankv.core.transaction.TradingPairImpl;
import me.frankv.core.transaction.TradingPairProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.TreeMap;

@Configuration
public class TradingPairConfig {

    @Bean
    public TradingPair testTradingPair(OrderRepository orderRepository) {
        var testPairProperties = new TradingPairProperties("test");
        return new TradingPairImpl(testPairProperties, orderRepository);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationRead(OrderRepository orderRepository) {
    }
}
