package me.frankv.core.util;

import me.frankv.core.entity.Order;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Supplier;

public class OrderUtils {
    public static final Comparator<Order> ORDER_DATE_COMPARATOR = Comparator.comparing((Order o) -> o.getId().getDate());
    public static final Supplier<TreeSet<Order>> ORDER_TREE_SET_SUPPLIER = () -> new TreeSet<>(ORDER_DATE_COMPARATOR);
}
