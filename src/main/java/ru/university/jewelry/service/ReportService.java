package ru.university.jewelry.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.SalesReportDTO;
import ru.university.jewelry.model.MetalType;
import ru.university.jewelry.model.Order;
import ru.university.jewelry.model.OrderItem;
import ru.university.jewelry.model.OrderStatus;
import ru.university.jewelry.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private static final List<OrderStatus> SOLD = List.of(OrderStatus.PAID, OrderStatus.SHIPPED, OrderStatus.DELIVERED);

    private final OrderRepository orderRepository;

    // в продажи попадают оплаченные, отправленные и доставленные заказы
    public SalesReportDTO sales(LocalDate from, LocalDate to) {
        LocalDateTime start = from == null ? LocalDateTime.of(2000, 1, 1, 0, 0) : from.atStartOfDay();
        LocalDateTime end = to == null ? LocalDateTime.now() : to.atTime(LocalTime.MAX);
        List<Order> orders = orderRepository.findByStatusInAndCreatedAtBetween(SOLD, start, end);

        BigDecimal revenue = orders.stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageCheck = orders.isEmpty()
                ? BigDecimal.ZERO
                : revenue.divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);

        Map<MetalType, BigDecimal> revenueByMetal = new EnumMap<>(MetalType.class);
        Map<MetalType, BigDecimal> weightByMetal = new EnumMap<>(MetalType.class);
        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                MetalType metal = item.getJewelry().getMetal();
                revenueByMetal.merge(metal, item.getPriceAtPurchase().multiply(quantity), BigDecimal::add);
                weightByMetal.merge(metal, item.getJewelry().getWeightGrams().multiply(quantity), BigDecimal::add);
            }
        }
        return new SalesReportDTO(from, to, orders.size(), revenue, averageCheck, revenueByMetal, weightByMetal);
    }
}
