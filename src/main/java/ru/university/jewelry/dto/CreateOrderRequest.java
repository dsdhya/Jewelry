package ru.university.jewelry.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotNull(message = "Укажите покупателя") Long userId,
        @NotEmpty(message = "В заказе должна быть хотя бы одна позиция") List<@Valid OrderItemRequest> items,
        @NotBlank(message = "Укажите адрес доставки") String deliveryAddress) {
}
