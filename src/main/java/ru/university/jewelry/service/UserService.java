package ru.university.jewelry.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.UserRequest;
import ru.university.jewelry.dto.UserResponse;
import ru.university.jewelry.exception.BusinessException;
import ru.university.jewelry.exception.NotFoundException;
import ru.university.jewelry.model.User;
import ru.university.jewelry.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserResponse register(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new BusinessException("Пользователь с email " + request.email() + " уже зарегистрирован");
        }
        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
                .build();
        return toResponse(repository.save(user));
    }

    public User getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getPhone());
    }
}
