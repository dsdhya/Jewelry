package ru.university.jewelry.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import ru.university.jewelry.model.MetalType;

public record SalesReportDTO(
        LocalDate from,
        LocalDate to,
        int ordersCount,
        BigDecimal revenue,
        BigDecimal averageCheck,
        Map<MetalType, BigDecimal> revenueByMetal,
        Map<MetalType, BigDecimal> weightSoldByMetal) {   // сколько грамм металла продано
}
