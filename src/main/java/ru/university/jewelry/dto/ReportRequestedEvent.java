package ru.university.jewelry.dto;

import java.time.LocalDateTime;

public record ReportRequestedEvent(String reportId, LocalDateTime requestedAt) {
}
