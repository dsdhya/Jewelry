package ru.university.jewelry.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.dto.CsvImportResult;
import ru.university.jewelry.dto.CsvImportRowError;
import ru.university.jewelry.exception.InvalidFileException;
import ru.university.jewelry.model.MetalType;

@Service
@RequiredArgsConstructor
public class JewelryImportService {
    private final JewelryRowImporter importer;

    public CsvImportResult importCsv(InputStream inputStream) {
        List<CsvImportRowError> errors = new ArrayList<>();
        int total = 0;
        int imported = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && (line.toLowerCase().startsWith("article") || line.toLowerCase().startsWith("title"))) {
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                total++;
                try {
                    String[] parts = line.split(",");
                    if (parts.length != 8) {
                        throw new InvalidFileException("Ожидалось 8 колонок (article,name,category,metal,hallmark,weightGrams,price,stockQuantity)");
                    }
                    importer.importRow(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            MetalType.valueOf(parts[3].trim().toUpperCase()),
                            Integer.parseInt(parts[4].trim()),
                            new BigDecimal(parts[5].trim()),
                            new BigDecimal(parts[6].trim()),
                            Integer.parseInt(parts[7].trim()));
                    imported++;
                } catch (Exception rowException) {
                    errors.add(new CsvImportRowError(lineNumber, line, rowException.getMessage()));
                }
            }
        } catch (IOException e) {
            throw new InvalidFileException("Не удалось прочитать CSV-файл: " + e.getMessage());
        }
        return new CsvImportResult(total, imported, total - imported, errors);
    }
}
