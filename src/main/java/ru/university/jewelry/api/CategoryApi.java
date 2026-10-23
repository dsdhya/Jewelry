package ru.university.jewelry.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.tags.Tag;
import ru.university.jewelry.dto.CategoryResponse;

@RequestMapping("/api/categories")
@Tag(name = "Категории")
public interface CategoryApi {

    @GetMapping
    List<CategoryResponse> getAll();
}
