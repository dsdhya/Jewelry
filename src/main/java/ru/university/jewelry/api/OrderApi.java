package ru.university.jewelry.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.university.jewelry.dto.ChangeOrderStatusRequest;
import ru.university.jewelry.dto.CreateOrderRequest;
import ru.university.jewelry.dto.OrderResponse;

@RequestMapping("/api")
@Tag(name = "Заказы")
public interface OrderApi {

    @Operation(summary = "Оформить заказ (списывает остаток со склада)")
    @PostMapping("/orders")
    OrderResponse create(@RequestBody @Valid CreateOrderRequest request);

    @Operation(summary = "История заказов покупателя")
    @GetMapping("/users/{userId}/orders")
    List<OrderResponse> getByUser(@PathVariable Long userId);

    @Operation(summary = "Сменить статус заказа")
    @PutMapping("/orders/{id}/status")
    OrderResponse changeStatus(@PathVariable Long id, @RequestBody @Valid ChangeOrderStatusRequest request);
}
