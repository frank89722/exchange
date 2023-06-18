package me.frankv.exchange.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.api.dto.request.OrderRequest;
import me.frankv.exchange.api.feign.CoreService;
import me.frankv.exchange.api.repository.MemberRepository;
import me.frankv.exchange.api.repository.TransactionRepository;
import me.frankv.exchange.api.service.TransactionService;
import me.frankv.exchange.api.service.WalletService;
import me.frankv.exchange.common.dto.OrderDto;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/test_pair")
@Slf4j
public class TestPairController {

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;
    private final CoreService coreService;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final MemberRepository memberRepository;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void addOrder(@RequestBody OrderRequest request) {
        transactionService.addOrder(request);
    }

    @GetMapping("/latest_price")
    public LatestPriceResponse getLatestPrice() {
        return new LatestPriceResponse(coreService.getLatestPrice("test"));
    }


    private record LatestPriceResponse(String latestPrice){}
}
