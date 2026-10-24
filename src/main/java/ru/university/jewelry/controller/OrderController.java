package ru.university.jewelry.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.api.OrderApi;
import ru.university.jewelry.dto.ChangeOrderStatusRequest;
import ru.university.jewelry.dto.CreateOrderRequest;
import ru.university.jewelry.dto.OrderResponse;
import ru.university.jewelry.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService service;

    @Override
    public OrderResponse create(CreateOrderRequest request) {
        return service.create(request);
    }

    @Override
    public List<OrderResponse> getByUser(Long userId) {
        return service.findByUser(userId);
    }

    @Override
    public OrderResponse changeStatus(Long id, ChangeOrderStatusRequest request) {
        return service.changeStatus(id, request.status());
    }
}
