package com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Slf4j
@Validated
public class OutboxRepository {
    private final OutboxJpaRepository outboxJpaRepository;

    public List<OutboxEvent> findNotProcessedEvents() {
        return outboxJpaRepository.findAllByProcessed(OutboxEvent.OutboxEventProcessed.FALSE);
    }

    public List<OutboxEvent> saveAll(List<OutboxEvent> events) {
        return outboxJpaRepository.saveAll(events);
    }
}
