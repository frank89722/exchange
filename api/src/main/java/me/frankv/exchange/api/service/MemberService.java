package me.frankv.exchange.api.service;

import lombok.RequiredArgsConstructor;
import me.frankv.exchange.api.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private MemberRepository repository;


}
