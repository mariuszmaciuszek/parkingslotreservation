package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Slf4j
@Validated
public class ReservationRepository {
    private final ReservationJpaRepository repository;

    public Optional<Reservation> findReservationByIdAndUserId(@NotNull UUID id, @NotNull UUID userId) {
        log.debug("findReservationByIdAndUserId({},{})", id, userId);
        return repository.findReservationByIdAndUserId(id, userId);
    }

    public Reservation save(@NotNull Reservation reservation) {
        log.debug("save({})", reservation);
        return repository.save(reservation);
    }

    public boolean hasUserCarIdActiveReservation(@NotNull UUID userCarId, @NotEmpty List<ReservationState> states) {
        log.debug("hasUserCarIdActiveReservation({},{})", userCarId, states);
        return repository.countReservationsByUserCarIdAndStateIn(userCarId, states) > 0;
    }

    public List<Reservation> findAllReservationsByUserId(@NotNull UUID userId) {
        log.debug("findAllReservationsByUserId({})", userId);
        return repository.findAllByUserId(userId);
    }

    public List<Reservation> findAllReservationAboutToStart(int inMinutes) {
        final LocalDateTime now = DateTimeUtils.now();
        return repository.findReservationsAboutToStartNotInOutbox(
                ReservationState.RESERVED,
                now,
                now.plusMinutes(inMinutes),
                OutboxEvent.OutboxEventType.RESERVATION_START_REMINDER);
    }

    public List<Reservation> findAllReservationAboutToEnd(int inMinutes) {
        final LocalDateTime now = DateTimeUtils.now();
        return repository.findReservationsAboutToEndNotInOutbox(
                ReservationState.IN_USE,
                now,
                now.plusMinutes(inMinutes),
                OutboxEvent.OutboxEventType.RESERVATION_END_REMINDER);
    }

    public List<Reservation> findAllReservationsInUseAfterEndedDate() {
        return repository.findByStateAndEndedDateBefore(ReservationState.IN_USE, DateTimeUtils.now());
    }

    public List<Reservation> findAllReservationsReservedAfterStartDate() {
        return repository.findByStateAndStartedDateBefore(ReservationState.RESERVED, DateTimeUtils.now());
    }

    public List<Reservation> saveAll(@NotNull List<Reservation> reservations) {
        log.debug("saveAll({})", reservations);
        return repository.saveAll(reservations);
    }
}
