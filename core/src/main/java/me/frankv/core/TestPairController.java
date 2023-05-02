package me.frankv.core;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.core.dto.OrderRequest;
import me.frankv.core.transaction.TradingPair;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/test_pair")
@Slf4j
public class TestPairController {

    private final TradingPair testTradingPair;
    private final Clock clock;
    private final KafkaTemplate<String, OrderRequest> kafkaTemplate;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void addTrade(@RequestBody OrderRequest request,
                         HttpServletResponse response) {

        kafkaTemplate.send("topic-order", request);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

}
