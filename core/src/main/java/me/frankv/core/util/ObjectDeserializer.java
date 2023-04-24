package me.frankv.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.core.dto.OrderDTO;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ObjectDeserializer<T> implements Deserializer<T> {

    private final Class<T> clazz;

    @Override
    public T deserialize(String topic, byte[] data) {
        var objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, clazz);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
}
