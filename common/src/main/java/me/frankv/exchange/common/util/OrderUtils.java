package me.frankv.exchange.common.util;


import me.frankv.exchange.common.model.Order;

import java.util.Comparator;
import java.util.TreeSet;

public class OrderUtils {
    public static final Comparator<Order> ORDER_DATE_COMPARATOR = Comparator.comparing((Order o) -> o.getId().getDate());

    public static TreeSet<Order> getOrderTreeSet() {
        return new TreeSet<>(ORDER_DATE_COMPARATOR);
    }
}
