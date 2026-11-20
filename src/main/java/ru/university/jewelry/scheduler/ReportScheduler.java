package ru.university.jewelry.scheduler;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.university.jewelry.dto.ReportRequestedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 10000)
    public void scheduleReportGeneration() {
        String reportId = UUID.randomUUID().toString();
        log.info("[Поток : {}] Инициирована генерация отчета {}",
                Thread.currentThread().getName(),
                reportId);

        eventPublisher.publishEvent(new ReportRequestedEvent(reportId, LocalDateTime.now()));
    }
}
