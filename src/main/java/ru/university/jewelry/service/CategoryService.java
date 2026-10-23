package ru.university.jewelry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.CategoryResponse;
import ru.university.jewelry.exception.NotFoundException;
import ru.university.jewelry.model.Category;
import ru.university.jewelry.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<CategoryResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public Category getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + id + " не найдена"));
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
