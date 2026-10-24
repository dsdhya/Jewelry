package ru.university.jewelry.dto;

import jakarta.validation.constraints.NotNull;
import ru.university.jewelry.model.OrderStatus;

public record ChangeOrderStatusRequest(
        @NotNull(message = "Укажите новый статус") OrderStatus status) {
}
