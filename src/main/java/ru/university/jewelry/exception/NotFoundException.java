package ru.university.jewelry.exception;

// объект не найден -> 404
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
