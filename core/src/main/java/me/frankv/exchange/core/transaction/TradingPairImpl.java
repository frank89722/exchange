package me.frankv.exchange.core.transaction;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.frankv.exchange.common.model.Order;
import me.frankv.exchange.common.util.OrderUtils;
import me.frankv.exchange.core.repository.OrderRepository;
import me.frankv.exchange.core.transaction.trader.Trader;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TradingPairImpl implements TradingPair {
    @Getter
    private final TradingPairProperties properties;

    private TreeMap<BigDecimal, TreeSet<Order>> sellOrders;
    private TreeMap<BigDecimal, TreeSet<Order>> buyOrders;
    private Map<Trader.TraderType, Trader> traders;

    private final OrderRepository repository;

    private final ReadWriteLock traderLock = new ReentrantReadWriteLock();
    private final Lock readLock = traderLock.readLock();
    private final Lock writeLock = traderLock.writeLock();

    @Getter
    private volatile BigDecimal latestPrice;

    private boolean ready;


    @Override
    public synchronized void init(List<Trader> traders) {
        if (isReady()) return;

        var sells = repository.getAllByType(Order.Direction.SELL);
        var buys = repository.getAllByType(Order.Direction.BUY);

        var collector = Collectors.groupingBy(
                Order::getPrice,
                TreeMap::new,
                Collectors.toCollection(OrderUtils::getOrderTreeSet));

        sellOrders = sells.parallelStream().collect(collector);
        buyOrders = buys.parallelStream().collect(collector);

        traders.forEach(t -> t.init(sellOrders, buyOrders));
        this.traders = Maps.uniqueIndex(traders, Trader::getType);

        ready = true;
    }

    @Override
    public void addOrder(@NonNull Order order) throws TradingPairNotReadyException {
        if (!isReady()) throw new TradingPairNotReadyException(properties.getName());
        synchronized (writeLock) {
            var trader = traders.get(Trader.TraderType.LIMIT);
            latestPrice = trader.trade(order);
        }
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
}
