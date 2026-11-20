package ru.university.jewelry.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.UserDto;
import ru.university.jewelry.dto.UserRequest;
import ru.university.jewelry.dto.UserResponse;
import ru.university.jewelry.exception.BusinessException;
import ru.university.jewelry.exception.NotFoundException;
import ru.university.jewelry.mapper.UserMapper;
import ru.university.jewelry.model.Role;
import ru.university.jewelry.model.User;
import ru.university.jewelry.repository.RoleRepository;
import ru.university.jewelry.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Пользователь с email " + request.email() + " уже зарегистрирован");
        }
        User user = User.builder()
                .username(request.email())
                .password(passwordEncoder.encode("user"))
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
                .build();
        return toResponse(userRepository.save(user));
    }

    public User getEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::userToUserDto).toList();
    }

    public UserDto create(UserDto userDto) {
        User user = UserMapper.userDtoToUser(userDto);

        Set<Role> roles = new HashSet<>();
        if (userDto.role() != null) {
            for (String roleTitle : userDto.role()) {
                Role role = roleRepository.findByTitle(roleTitle).orElseThrow(
                        () -> new NotFoundException("Role not found: " + roleTitle));
                roles.add(role);
            }
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userDto.password()));

        return UserMapper.userToUserDto(userRepository.save(user));
    }

    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
        return UserMapper.userToUserDto(user);
    }

    public UserDto getUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found"));
        return UserMapper.userToUserDto(user);
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));

        Set<Role> roles = new HashSet<>();
        if (userDto.role() != null) {
            for (String roleTitle : userDto.role()) {
                Role role = roleRepository.findByTitle(roleTitle).orElseThrow(
                        () -> new NotFoundException("Role not found: " + roleTitle));
                roles.add(role);
            }
        }

        user.setUsername(userDto.username());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRoles(roles);

        return UserMapper.userToUserDto(userRepository.save(user));
    }

    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found"));
        userRepository.delete(user);
        return String.format("User with %d deleted successfully", userId);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getPhone());
    }
}
