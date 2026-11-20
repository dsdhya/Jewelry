package ru.university.jewelry.dto;

public record CsvImportRowError(int lineNumber, String rawLine, String message) {
}
