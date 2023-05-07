package me.frankv.exchange.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.api.feign.CoreService;
import me.frankv.exchange.common.dto.OrderRequest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/test_pair")
@Slf4j
public class TestPairController {

    private final KafkaTemplate<String, OrderRequest> kafkaTemplate;
    private final CoreService coreService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void addTrade(@RequestBody OrderRequest request) {

        kafkaTemplate.send("topic-order", request);
    }

    @GetMapping("/latest_price")
    public LatestPriceDto getLatestPrice() {
        return new LatestPriceDto(coreService.getLatestPrice("test"));
    }

    private record LatestPriceDto(String latestPrice){}
}
