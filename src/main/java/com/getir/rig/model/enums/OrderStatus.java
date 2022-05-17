package com.getir.rig.model.enums;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {
    NEW,
    SHIPPED,
    CANCELLED,
    RETURNED,
    COMPLETED;

    public static Optional<OrderStatus> of(final String name) {
        return Arrays.stream(OrderStatus.values()).filter(authority -> authority.name().equals(name)).findFirst();
    }
}
