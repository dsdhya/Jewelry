package ru.university.jewelry.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.api.JewelryApi;
import ru.university.jewelry.dto.JewelryFilter;
import ru.university.jewelry.dto.JewelryRequest;
import ru.university.jewelry.dto.JewelryResponse;
import ru.university.jewelry.service.JewelryService;

@RestController
@RequiredArgsConstructor
public class JewelryController implements JewelryApi {

    private final JewelryService service;

    @Override
    public List<JewelryResponse> getAll(JewelryFilter filter) {
        return service.findAll(filter);
    }

    @Override
    public JewelryResponse get(Long id) {
        return service.findById(id);
    }

    @Override
    public JewelryResponse create(JewelryRequest request) {
        return service.create(request);
    }

    @Override
    public JewelryResponse update(JewelryRequest request, Long id) {
        return service.update(request, id);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
