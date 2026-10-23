package ru.university.jewelry.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long jewelryId,
        String jewelryName,
        int quantity,
        BigDecimal priceAtPurchase,
        BigDecimal subtotal) {
}
