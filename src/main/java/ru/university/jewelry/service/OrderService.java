package ru.university.jewelry.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.CreateOrderRequest;
import ru.university.jewelry.dto.OrderItemRequest;
import ru.university.jewelry.dto.OrderItemResponse;
import ru.university.jewelry.dto.OrderResponse;
import ru.university.jewelry.exception.BusinessException;
import ru.university.jewelry.exception.NotFoundException;
import ru.university.jewelry.model.Jewelry;
import ru.university.jewelry.model.JewelryStatus;
import ru.university.jewelry.model.Order;
import ru.university.jewelry.model.OrderItem;
import ru.university.jewelry.model.OrderStatus;
import ru.university.jewelry.repository.OrderRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final UserService userService;
    private final JewelryService jewelryService;

    // оформление заказа: проверяем остатки, списываем склад, фиксируем цены
    public OrderResponse create(CreateOrderRequest request) {
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.items()) {
            Jewelry jewelry = jewelryService.getEntity(itemRequest.jewelryId());
            if (jewelry.getStatus() == JewelryStatus.DISCONTINUED) {
                throw new BusinessException("Изделие " + jewelry.getName() + " снято с продажи");
            }
            if (itemRequest.quantity() > jewelry.getStockQuantity()) {
                throw new BusinessException("Недостаточно на складе: " + jewelry.getName()
                        + ", осталось " + jewelry.getStockQuantity() + " шт.");
            }
            items.add(OrderItem.builder()
                    .jewelry(jewelry)
                    .quantity(itemRequest.quantity())
                    .priceAtPurchase(jewelry.getPrice())
                    .build());
        }
        // склад списываем только после проверки всех позиций
        items.forEach(i -> jewelryService.changeStock(i.getJewelry(), -i.getQuantity()));

        Order order = Order.builder()
                .user(userService.getEntity(request.userId()))
                .items(items)
                .totalAmount(total(items))
                .deliveryAddress(request.deliveryAddress())
                .status(OrderStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();
        return toResponse(repository.save(order));
    }

    public List<OrderResponse> findByUser(Long userId) {
        userService.getEntity(userId);
        return repository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toResponse).toList();
    }

    // правило: статусы меняются только по цепочке NEW -> CONFIRMED -> PAID -> SHIPPED -> DELIVERED
    public OrderResponse changeStatus(Long id, OrderStatus newStatus) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ с id=" + id + " не найден"));
        OrderStatus current = order.getStatus();
        if (!current.canChangeTo(newStatus)) {
            throw new BusinessException("Нельзя перевести заказ из " + current + " в " + newStatus
                    + ". Допустимо: " + current.nextAllowed());
        }
        // при отмене и возврате изделия возвращаются на склад
        if (newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.RETURNED) {
            order.getItems().forEach(i -> jewelryService.changeStock(i.getJewelry(), i.getQuantity()));
        }
        order.setStatus(newStatus);
        return toResponse(repository.save(order));
    }

    private BigDecimal total(List<OrderItem> items) {
        return items.stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getItems().stream()
                        .map(i -> new OrderItemResponse(
                                i.getJewelry().getId(),
                                i.getJewelry().getName(),
                                i.getQuantity(),
                                i.getPriceAtPurchase(),
                                i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity()))))
                        .toList(),
                order.getTotalAmount(),
                order.getDeliveryAddress(),
                order.getStatus(),
                order.getCreatedAt());
    }
}
