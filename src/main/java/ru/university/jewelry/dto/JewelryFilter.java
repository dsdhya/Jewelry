package ru.university.jewelry.dto;

import java.math.BigDecimal;

import ru.university.jewelry.model.MetalType;

// параметры фильтра каталога, все необязательные
public record JewelryFilter(
        Long categoryId,
        MetalType metal,
        Integer hallmark,
        BigDecimal size,
        BigDecimal minPrice,
        BigDecimal maxPrice) {
}
