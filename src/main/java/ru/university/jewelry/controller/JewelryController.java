package ru.university.jewelry.controller;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.api.JewelryApi;
import ru.university.jewelry.dto.CsvImportResult;
import ru.university.jewelry.dto.JewelryFilter;
import ru.university.jewelry.dto.JewelryRequest;
import ru.university.jewelry.dto.JewelryResponse;
import ru.university.jewelry.report.JewelryImportService;
import ru.university.jewelry.service.JewelryService;

@RestController
@RequiredArgsConstructor
public class JewelryController implements JewelryApi {

    private final JewelryService service;
    private final JewelryImportService importService;

    @Override
    public Page<JewelryResponse> getAll(JewelryFilter filter, Pageable pageable) {
        return service.findAll(filter, pageable);
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
    public String uploadPhoto(Long id, MultipartFile file) {
        return service.uploadPhoto(id, file);
    }

    @Override
    public CsvImportResult importCsv(MultipartFile file) {
        try {
            return importService.importCsv(file.getInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
