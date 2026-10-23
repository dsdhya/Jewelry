package ru.university.jewelry.api;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.university.jewelry.dto.SalesReportDTO;

@RequestMapping("/api/reports")
@Tag(name = "Отчёты")
public interface ReportApi {

    @Operation(summary = "Отчёт по продажам: выручка, средний чек, продажи по металлам")
    @GetMapping("/sales")
    SalesReportDTO sales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to);
}
