package ru.university.jewelry.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull(message = "Укажите изделие") Long jewelryId,
        @Min(value = 1, message = "Количество должно быть не меньше 1") int quantity) {
}
