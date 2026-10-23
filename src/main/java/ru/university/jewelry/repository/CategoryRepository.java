package ru.university.jewelry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.university.jewelry.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
