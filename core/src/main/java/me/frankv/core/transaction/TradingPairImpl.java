package me.frankv.core.transaction;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.core.entity.Order;
import me.frankv.core.repository.OrderRepository;
import me.frankv.core.util.OrderUtils;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TradingPairImpl implements TradingPair {
    private final TradingPairProperties properties;

    private TreeMap<BigDecimal, TreeSet<Order>> sellOrders;
    private TreeMap<BigDecimal, TreeSet<Order>> buyOrders;

    private final OrderRepository repository;

    private final ReadWriteLock traderLock = new ReentrantReadWriteLock();
    private final Lock readLock = traderLock.readLock();
    private final Lock writeLock = traderLock.writeLock();

    @Getter
    private volatile BigDecimal latestPrice;

    private boolean ready;


    @Override
    public synchronized void init() {
        if (isReady()) return;

        var sells = repository.getAllByType(Order.Type.SELL);
        var buys = repository.getAllByType(Order.Type.BUY);

        var collector = Collectors.groupingBy(
                Order::getPrice,
                TreeMap::new,
                Collectors.toCollection(OrderUtils.ORDER_TREE_SET_SUPPLIER));

        sellOrders = sells.parallelStream().collect(collector);
        buyOrders = buys.parallelStream().collect(collector);

        ready = true;
    }

    @Override
    public void addOrder(@NonNull Order order) throws TradingPairNotReadyException {
        if (!isReady()) throw new TradingPairNotReadyException(properties.getName());
        trade(getTradableOrders(order), order, this::writeOrder, this::wipeOrder);
    }

    @Override
    public boolean isReady() {
        return ready && buyOrders != null && sellOrders != null;
    }

    @Override
    public void removeOrder(Order order) {

    }

    @Override
    public void removeOrder(String orderId) {

    }

    @Override
    public void removeOrder(ObjectId orderId) {

    }

    @Override
    public void removeOrderByMemberId(String memberId) {

    }

    private List<Order> getTradableOrders(Order order) {
        synchronized (readLock) {
            return (order.getType() == Order.Type.SELL ? buyOrders.descendingMap() : sellOrders)
                    .entrySet().stream()
                    .filter(o -> o.getKey().compareTo(order.getAmount()) < 1)
                    .flatMap(o -> o.getValue().stream())
                    .toList();
        }
    }

    private void trade(List<Order> tradable,
                       Order order,
                       Consumer<Order> orderSaver,
                       Consumer<Order> existedOrderRemover)
    {
        if (tradable.size() == 0) return;

        int tradableIndex = 0;
        int tradeCount = 0;
        BigDecimal latest = getLatestPrice();

        synchronized (writeLock) {
            while (tradableIndex < tradable.size() && !order.getAmount().equals(BigDecimal.ZERO)) {
                var existedOrder = tradable.get(tradableIndex);

                if (order.getAmount().compareTo(existedOrder.getAmount()) >= 0) {
                    order.setAmount(order.getAmount().subtract(existedOrder.getAmount()));
                    existedOrderRemover.accept(existedOrder);
                    tradableIndex++;
                } else {
                    existedOrder.setAmount(existedOrder.getAmount().subtract(order.getAmount()));
                    repository.save(existedOrder);
                    order.setAmount(BigDecimal.ZERO);
                }

                tradeCount++;
                latest = existedOrder.getPrice();
            }

            latestPrice = latest;

            if (!order.getAmount().equals(BigDecimal.ZERO)) {
                orderSaver.accept(order);
            }
        }

        log.info(String.format("Trade count: %d", tradeCount));
    }

    private void writeOrder(Order order) {
        synchronized (writeLock) {
            repository.save(order);
            addOrderToMap(order);
        }
    }

    private void wipeOrder(Order order) {
        synchronized (writeLock) {
            repository.deleteById(order.getId());
            removeOrderFromMap(order);
        }
    }

    private void removeOrderFromMap(Order order) {
        synchronized (writeLock) {
            (order.getType() == Order.Type.SELL ? sellOrders : buyOrders)
                    .get(order.getPrice()).remove(order);
        }
    }

    private void addOrderToMap(Order order) {
        synchronized (writeLock) {
            var orders = (order.getType() == Order.Type.SELL ? sellOrders : buyOrders);
            var set = orders.computeIfAbsent(order.getPrice(), k -> OrderUtils.ORDER_TREE_SET_SUPPLIER.get());
            set.add(order);
        }
    }

    public TreeMap<BigDecimal, TreeSet<Order>> getSellOrders() {
        try {
            readLock.lock();
            return sellOrders;
        } finally {
            readLock.unlock();
        }
    }

    public TreeMap<BigDecimal, TreeSet<Order>> getBuyOrders() {
        try {
            readLock.lock();
            return buyOrders;
        } finally {
            readLock.unlock();
        }
    }
}
