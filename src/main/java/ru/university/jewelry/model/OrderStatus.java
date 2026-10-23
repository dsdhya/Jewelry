package ru.university.jewelry.model;

import java.util.Set;

public enum OrderStatus {
    NEW, CONFIRMED, PAID, SHIPPED, DELIVERED, CANCELLED, RETURNED;

    // в какие статусы можно перейти из текущего
    public Set<OrderStatus> nextAllowed() {
        return switch (this) {
            case NEW -> Set.of(CONFIRMED, CANCELLED);
            case CONFIRMED -> Set.of(PAID, CANCELLED);
            case PAID -> Set.of(SHIPPED, CANCELLED);
            case SHIPPED -> Set.of(DELIVERED);
            case DELIVERED -> Set.of(RETURNED);
            case CANCELLED, RETURNED -> Set.of();
        };
    }

    public boolean canChangeTo(OrderStatus next) {
        return nextAllowed().contains(next);
    }
}
