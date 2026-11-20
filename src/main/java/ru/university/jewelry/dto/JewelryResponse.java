package ru.university.jewelry.dto;

import java.math.BigDecimal;

import ru.university.jewelry.model.JewelryStatus;
import ru.university.jewelry.model.MetalType;

public record JewelryResponse(
        Long id,
        String article,
        String name,
        CategoryResponse category,
        MetalType metal,
        int hallmark,
        BigDecimal weightGrams,
        BigDecimal size,
        String stone,
        BigDecimal price,
        int stockQuantity,
        JewelryStatus status,
        String photoUrl) {
}
