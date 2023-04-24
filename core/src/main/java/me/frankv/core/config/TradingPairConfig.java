package me.frankv.core.config;

import me.frankv.core.dto.OrderDTO;
import me.frankv.core.transaction.TradingPair;
import me.frankv.core.transaction.TradingPairImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.TreeMap;

@Configuration
public class TradingPairConfig {

    @Bean
    public TradingPair testTradingPair(KafkaTemplate<String, OrderDTO> kafkaTemplate) {
        return new TradingPairImpl(new TreeMap<>(), new TreeMap<>(), kafkaTemplate);
    }
}
