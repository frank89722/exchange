package me.frankv.exchange.api.config;

import me.frankv.exchange.common.dto.OrderRequest;
import me.frankv.exchange.common.util.ObjectSerializer;
import me.frankv.exchange.common.util.OrderRequestDeserializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    @Bean
    public KafkaTemplate<String, OrderRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, OrderRequest> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, OrderRequest> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, OrderRequestDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "trader");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new OrderRequestDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderRequest> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderRequest>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public KafkaAdmin admin(KafkaProperties properties){
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }

    @Bean
    @Profile("dev")
    public NewTopic topic() {
        return new NewTopic("topic-order", 1, (short) 1);
    }
}
