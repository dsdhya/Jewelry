package ru.university.jewelry.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import ru.university.jewelry.model.OrderStatus;

public record OrderResponse(
        Long id,
        Long userId,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        String deliveryAddress,
        OrderStatus status,
        LocalDateTime createdAt) {
}
