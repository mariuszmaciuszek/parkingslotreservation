package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.occupiedreminder;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository.OutboxRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.ReminderService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationOccupiedStartedManager implements ReminderService {
    private final ReservationRepository reservationRepository;
    private final OutboxRepository outboxRepository;

    @Override
    @Transactional
    public void process() {
        final List<Reservation> reservationsNotFinished =
                reservationRepository.findAllReservationsInUseAfterEndedDate();
        final List<Reservation> reservationsNotStarted =
                reservationRepository.findAllReservationsReservedAfterStartDate();
        final List<Reservation> reservations = Stream.concat(
                        reservationsNotFinished.stream(), reservationsNotStarted.stream())
                .toList();

        if (reservations.isEmpty()) {
            return;
        }

        final List<OutboxEvent> eventList = reservations.stream()
                .map(r -> OutboxEvent.builder()
                        .reservationId(r.getId())
                        .eventType(OutboxEvent.OutboxEventType.RESERVATION_OCCUPIED)
                        .id(UUID.randomUUID())
                        .build())
                .toList();
        reservations.forEach(Reservation::occupiedReservation);

        log.info("Occupied started for: {}", reservations);
        reservationRepository.saveAll(reservations);
        outboxRepository.saveAll(eventList);
    }
}
