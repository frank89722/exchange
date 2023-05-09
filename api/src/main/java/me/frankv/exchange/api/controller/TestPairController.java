package me.frankv.exchange.api.controller;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.api.entity.TransactionEntity;
import me.frankv.exchange.api.entity.WalletEntity;
import me.frankv.exchange.api.feign.CoreService;
import me.frankv.exchange.api.repository.MemberRepository;
import me.frankv.exchange.api.repository.TransactionRepository;
import me.frankv.exchange.api.repository.WalletRepository;
import me.frankv.exchange.common.dto.OrderDto;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/test_pair")
@Slf4j
public class TestPairController {

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;
    private final CoreService coreService;
    private final WalletRepository walletRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    private final String product = "test";
    private final String currency = "u";

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void addOrder(@RequestBody OrderRequest request) {
        var member = memberRepository.findAllById(new ObjectId(request.memberId))
                .orElseThrow();

        var prodWallet = walletRepository.findAllByMemberIdAndTokenName(new ObjectId(request.memberId), product)
                .orElse(createNewWallet(member.getId(), product));

        var currWallet = walletRepository.findAllByMemberIdAndTokenName(new ObjectId(request.memberId), currency)
                .orElse(createNewWallet(member.getId(), currency));

        var wallet = switch (request.type()) {
            case "buy" -> currWallet;
            case "sell" -> prodWallet;
            default -> throw new IllegalArgumentException();
        };

        var transaction = createTransaction(wallet, request);
        if (transaction == null) throw new RuntimeException();
    }

    @GetMapping("/latest_price")
    public LatestPriceResponse getLatestPrice() {
        return new LatestPriceResponse(coreService.getLatestPrice("test"));
    }

    @Transactional(rollbackOn = Exception.class)
    public TransactionEntity createTransaction(WalletEntity wallet, OrderRequest request) throws RuntimeException {
        var amount = new BigDecimal(request.amount());
        if (wallet.getAmount().compareTo(amount) < 0) return null;

        var transaction = TransactionEntity.builder()
                .id(new ObjectId())
                .memberId(wallet.getMemberId())
                .tradingPairName(request.tradingPairName)
                .price(new BigDecimal(request.price()))
                .amount(amount)
                .build();

        wallet.setAmount(wallet.getAmount().subtract(transaction.getAmount()));
        transactionRepository.save(transaction);
        walletRepository.save(wallet);

        kafkaTemplate.send("order-topic",
                OrderDto.builder()
                        .id(transaction.getId().toString())
                        .price(request.price())
                        .amount(request.amount)
                        .type(request.type())
                        .build());

        return transaction;
    }

    private WalletEntity createNewWallet(ObjectId memberId, String tokenName) {
        var wallet = WalletEntity.builder()
                .id(new ObjectId())
                .memberId(memberId)
                .amount(BigDecimal.ZERO)
                .tokenName(tokenName)
                .build();

        walletRepository.save(wallet);
        return wallet;
    }

    private record LatestPriceResponse(String latestPrice){}
    private record OrderRequest(
            @NonNull String memberId,
            @NonNull String amount,
            @NonNull String price,
            @NonNull String type,
            @NonNull String tradingPairName
    ){}
}
