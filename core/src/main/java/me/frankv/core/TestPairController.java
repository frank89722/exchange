package me.frankv.core;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.core.entity.Order;
import me.frankv.core.transaction.TradingPair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/test_pair")
@Slf4j
public class TestPairController {

    private final TradingPair testTradingPair;
    private final Clock clock;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void addTrade(@RequestBody OrderRequest request,
                         HttpServletResponse response)
    {
        var order = Order.builder()
                .amount(new BigDecimal(request.price))
                .price(new BigDecimal(request.amount))
                .type(switch (request.type) {
                    case "buy" -> Order.Type.BUY;
                    case "sell" -> Order.Type.SELL;
                    default -> throw new IllegalArgumentException(String.format("type '%s' not found", request.type));
                })
                .localDateTime(LocalDateTime.now(clock))
                .build();

        testTradingPair.addOrder(order);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    private record OrderRequest(
            String price,
            String amount,
            String type
    ){}
}
