package me.frankv.exchange.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(name = "core", url = "http://localhost:8080/v1")
public interface CoreService {
    @GetMapping(value = "/latest_price/{tradingPair}")
    String getLatestPrice(@PathVariable String tradingPair);
}
