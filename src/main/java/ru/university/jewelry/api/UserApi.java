package ru.university.jewelry.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.university.jewelry.dto.UserRequest;
import ru.university.jewelry.dto.UserResponse;

@RequestMapping("/api/users")
@Tag(name = "Покупатели")
public interface UserApi {

    @Operation(summary = "Зарегистрировать покупателя")
    @PostMapping
    UserResponse register(@RequestBody @Valid UserRequest request);
}
