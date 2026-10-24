package ru.university.jewelry.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import ru.university.jewelry.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
        return build(HttpStatus.NOT_FOUND, List.of(e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        return build(HttpStatus.BAD_REQUEST, List.of(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, messages);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidation(HandlerMethodValidationException e) {
        List<String> messages = e.getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, messages);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, List<String> messages) {
        ErrorResponse body = new ErrorResponse(status.value(), status.getReasonPhrase(), messages, LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }
}
