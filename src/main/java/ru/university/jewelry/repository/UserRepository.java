package ru.university.jewelry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.university.jewelry.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String userName);
}
