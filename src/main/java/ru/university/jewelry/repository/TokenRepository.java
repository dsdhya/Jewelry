package ru.university.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.university.jewelry.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
