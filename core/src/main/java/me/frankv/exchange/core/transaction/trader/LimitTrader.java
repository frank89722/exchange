package me.frankv.exchange.core.transaction.trader;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.core.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Builder
public class LimitTrader implements Trader {

    private final Consumer<OrderEntity> orderWriter;
    private final Consumer<OrderEntity> orderWiper;
    private final Consumer<OrderEntity> orderUpdater;
    private final Supplier<BigDecimal> lastestPriceSupplier;
    private TreeMap<BigDecimal, TreeSet<OrderEntity>> sellOrders;
    private TreeMap<BigDecimal, TreeSet<OrderEntity>> buyOrders;

    @Override
    public BigDecimal trade(List<OrderEntity> tradables, OrderEntity orderEntity) {
        if (tradables.size() == 0) {
            orderWriter.accept(orderEntity);
            return lastestPriceSupplier.get();
        }

        int tradableIndex = 0;
        int tradeCount = 0;
        BigDecimal latest = lastestPriceSupplier.get();

        while (tradableIndex < tradables.size() && !orderEntity.getAmount().equals(BigDecimal.ZERO)) {
            var existedOrder = tradables.get(tradableIndex);

            if (orderEntity.getAmount().compareTo(existedOrder.getAmount()) >= 0) {
                orderEntity.setAmount(orderEntity.getAmount().subtract(existedOrder.getAmount()));
                existedOrder.setAmount(BigDecimal.ZERO);
                orderWiper.accept(existedOrder);
                tradableIndex++;
            } else {
                existedOrder.setAmount(existedOrder.getAmount().subtract(orderEntity.getAmount()));
                orderUpdater.accept(existedOrder);
                orderEntity.setAmount(BigDecimal.ZERO);
            }

            tradeCount++;
            latest = existedOrder.getPrice();
        }

        if (!orderEntity.getAmount().equals(BigDecimal.ZERO)) {
            orderWriter.accept(orderEntity);
        }

        log.info(String.format("Trade count: %d", tradeCount));
        return latest;
    }

}
