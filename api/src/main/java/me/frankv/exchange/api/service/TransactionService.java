package me.frankv.exchange.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.frankv.exchange.api.dto.request.OrderRequest;
import me.frankv.exchange.api.entity.TransactionEntity;
import me.frankv.exchange.api.entity.WalletEntity;
import me.frankv.exchange.api.repository.MemberRepository;
import me.frankv.exchange.api.repository.TransactionRepository;
import me.frankv.exchange.api.repository.WalletRepository;
import me.frankv.exchange.common.dto.OrderDto;
import org.bson.types.ObjectId;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final WalletRepository walletRepository;
    private final MemberRepository memberRepository;
    private final WalletService walletService;
    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    private final String product = "test";
    private final String currency = "u";

    @Transactional(rollbackOn = Exception.class)
    public void addOrder(OrderRequest request) throws RuntimeException {
        var member = memberRepository.findAllById(new ObjectId(request.memberId()))
                .orElseThrow();

        var memberId = new ObjectId(request.memberId());

        var wallet = switch (request.type()) {
            case "buy" -> walletRepository.findAllByMemberIdAndTokenName(memberId, product)
                    .orElse(walletService.createNewWallet(member.getId(), product));
            case "sell" -> walletRepository.findAllByMemberIdAndTokenName(memberId, currency)
                    .orElse(walletService.createNewWallet(member.getId(), currency));
            default -> throw new IllegalArgumentException();
        };

        var amount = new BigDecimal(request.amount());
        if (wallet.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("Not enough money in wallet");
        }

        wallet.setAmount(wallet.getAmount().subtract(amount));
        createTransaction(wallet, request);
    }

    private TransactionEntity createTransaction(WalletEntity wallet, OrderRequest request) throws RuntimeException {
        var transaction = TransactionEntity.builder()
                .id(new ObjectId())
                .memberId(wallet.getMemberId())
                .tradingPairName(request.tradingPairName())
                .price(new BigDecimal(request.price()))
                .amount(new BigDecimal(request.amount()))
                .build();

        kafkaTemplate.send("order-topic",
                OrderDto.builder()
                        .id(transaction.getId().toString())
                        .price(request.price())
                        .amount(request.amount())
                        .type(request.type())
                        .build());

        walletRepository.save(wallet);
        return repository.save(transaction);
    }

}
