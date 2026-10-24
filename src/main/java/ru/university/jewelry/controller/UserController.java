package ru.university.jewelry.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.api.UserApi;
import ru.university.jewelry.dto.UserRequest;
import ru.university.jewelry.dto.UserResponse;
import ru.university.jewelry.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService service;

    @Override
    public UserResponse register(UserRequest request) {
        return service.register(request);
    }
}
