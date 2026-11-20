package ru.university.jewelry.dto;

import java.util.List;

public record CsvImportResult(int totalRows,
        int imported, int failed, List<CsvImportRowError> errors) {
}
