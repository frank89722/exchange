package me.frankv.exchange.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;

@Slf4j
@NoArgsConstructor
public class ObjectSerializer implements Serializer<Object> {

    @Override
    public byte[] serialize(String topic, Object data) {
        var objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
}
