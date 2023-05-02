package me.frankv.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.core.dto.OrderRequest;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
@NoArgsConstructor
public class OrderRequestDeserializer implements Deserializer<OrderRequest> {

    @Override
    public OrderRequest deserialize(String topic, byte[] data) {
        var objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, OrderRequest.class);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
}
