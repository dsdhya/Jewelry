package ru.university.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.university.jewelry.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
