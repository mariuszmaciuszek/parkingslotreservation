package com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.scheduler;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository.OutboxRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxManager implements OutboxService {
    private final OutboxRepository outboxRepository;

    @Override
    public void processEvents() {
        final List<OutboxEvent> notProcessedEvents = outboxRepository.findNotProcessedEvents();
        if (notProcessedEvents.isEmpty()) {
            return;
        }
        log.info("Events to process {}", notProcessedEvents);
        notProcessedEvents.forEach(OutboxEvent::markProcessed);
        outboxRepository.saveAll(notProcessedEvents);
    }
}
