package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.abouttoendreminder;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository.OutboxRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.ReminderService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationAboutToEndManager implements ReminderService {
    private final ReservationRepository reservationRepository;
    private final OutboxRepository outboxRepository;

    @Value("${app.scheduler.inMinutesPeriod}")
    private int inMinutesPeriod;

    @Override
    public void process() {
        final List<OutboxEvent> eventList = reservationRepository.findAllReservationAboutToEnd(inMinutesPeriod).stream()
                .map(r -> OutboxEvent.builder()
                        .reservationId(r.getId())
                        .eventType(OutboxEvent.OutboxEventType.RESERVATION_END_REMINDER)
                        .id(UUID.randomUUID())
                        .build())
                .toList();
        if (eventList.isEmpty()) {
            return;
        }
        log.debug("Creating reminder events: {}", eventList);
        outboxRepository.saveAll(eventList);
    }
}
