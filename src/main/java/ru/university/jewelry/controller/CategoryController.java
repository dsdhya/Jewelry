package ru.university.jewelry.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.api.CategoryApi;
import ru.university.jewelry.dto.CategoryResponse;
import ru.university.jewelry.service.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryService service;

    @Override
    public List<CategoryResponse> getAll() {
        return service.findAll();
    }
}
