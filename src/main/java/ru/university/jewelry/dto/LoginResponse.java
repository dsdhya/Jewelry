package ru.university.jewelry.dto;

public record LoginResponse(
        boolean isLogged,
        String roles) {
}
