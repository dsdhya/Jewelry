package ru.university.jewelry.api;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ru.university.jewelry.dto.JewelryFilter;
import ru.university.jewelry.dto.JewelryRequest;
import ru.university.jewelry.dto.JewelryResponse;

@RequestMapping("/api/jewelry")
@Tag(name = "Каталог изделий")
public interface JewelryApi {

    @Operation(summary = "Каталог с фильтрами по категории, металлу, пробе, размеру и цене")
    @GetMapping
    List<JewelryResponse> getAll(@ParameterObject JewelryFilter filter);

    @Operation(summary = "Карточка изделия")
    @GetMapping("/{id}")
    JewelryResponse get(@PathVariable Long id);

    @Operation(summary = "Добавить изделие")
    @PostMapping
    JewelryResponse create(@RequestBody @Valid JewelryRequest request);

    @Operation(summary = "Изменить изделие")
    @PutMapping("/{id}")
    JewelryResponse update(@RequestBody @Valid JewelryRequest request, @PathVariable Long id);

    @Operation(summary = "Снять изделие с продажи")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
