package ru.university.jewelry.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "ФИО не может быть пустым") String fullName,
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный email") String email,
        String phone) {
}
