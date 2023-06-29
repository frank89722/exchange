package me.frankv.exchange.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.common.dto.OrderDto;
import me.frankv.exchange.common.dto.TransactionDto;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
@NoArgsConstructor
public class TransactionDtoDeserializer implements Deserializer<TransactionDto> {

    @Override
    public TransactionDto deserialize(String topic, byte[] data) {
        var objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, TransactionDto.class);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
}
