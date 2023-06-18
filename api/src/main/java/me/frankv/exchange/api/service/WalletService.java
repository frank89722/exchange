package me.frankv.exchange.api.service;

import lombok.RequiredArgsConstructor;
import me.frankv.exchange.api.entity.WalletEntity;
import me.frankv.exchange.api.repository.MemberRepository;
import me.frankv.exchange.api.repository.WalletRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;

    public WalletEntity createNewWallet(ObjectId memberId, String tokenName) {
        var wallet = WalletEntity.builder()
                .id(new ObjectId())
                .memberId(memberId)
                .amount(BigDecimal.ZERO)
                .tokenName(tokenName)
                .build();

        repository.save(wallet);
        return wallet;
    }
}
