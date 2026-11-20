package ru.university.jewelry.subcriber;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.university.jewelry.dto.ReportComplitedEvent;
import ru.university.jewelry.dto.ReportRequestedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportEventSubscriber {
    private final ApplicationEventPublisher eventPublisher;

    @Async("taskExecutor")
    @EventListener
    public void handleReportRequested(ReportRequestedEvent event) {
        log.info("[Поток : {}] Начата генерация отчета {} от {}",
                Thread.currentThread().getName(),
                event.reportId(),
                event.requestedAt());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        log.info("[Поток : {}] Выполнена генерация отчета {}",
                Thread.currentThread().getName(),
                event.reportId());
        eventPublisher.publishEvent(new ReportComplitedEvent(event.reportId(), "COMPLETED"));
    }
}
