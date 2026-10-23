package ru.university.jewelry.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.university.jewelry.api.ReportApi;
import ru.university.jewelry.dto.SalesReportDTO;
import ru.university.jewelry.service.ReportService;

@RestController
@RequiredArgsConstructor
public class ReportController implements ReportApi {

    private final ReportService service;

    @Override
    public SalesReportDTO sales(LocalDate from, LocalDate to) {
        return service.sales(from, to);
    }
}
