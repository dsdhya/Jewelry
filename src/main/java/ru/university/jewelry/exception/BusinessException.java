package ru.university.jewelry.exception;

// нарушено бизнес-правило (нет на складе, неверная проба, недопустимый статус) -> 400
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
