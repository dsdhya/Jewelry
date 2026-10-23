package ru.university.jewelry.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.university.jewelry.model.MetalType;

public record JewelryRequest(
        @NotBlank(message = "Артикул не может быть пустым") String article,
        @NotBlank(message = "Название не может быть пустым") String name,
        @NotNull(message = "Укажите категорию") Long categoryId,
        @NotNull(message = "Укажите металл") MetalType metal,
        @NotNull(message = "Укажите пробу") Integer hallmark,
        @NotNull(message = "Укажите вес")
        @DecimalMin(value = "0.01", message = "Вес должен быть больше 0") BigDecimal weightGrams,
        BigDecimal size,
        String stone,
        @NotNull(message = "Укажите цену")
        @DecimalMin(value = "0.01", message = "Цена должна быть больше 0") BigDecimal price,
        @Min(value = 0, message = "Остаток не может быть отрицательным") int stockQuantity) {
}
