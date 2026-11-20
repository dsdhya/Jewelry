package ru.university.jewelry.dto;

import java.util.Set;

public record UserLoggedDto(String username, Set<String> role, Set<String> permissions) {
}
