package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, UUID> {

    Optional<Reservation> findReservationByIdAndUserId(UUID id, UUID userId);

    long countReservationsByUserCarIdAndStateIn(UUID userCarId, List<ReservationState> states);

    List<Reservation> findAllByUserId(UUID userId);

    @Query("SELECT r FROM Reservation r " + "WHERE r.state = :rstate "
            + "AND r.endedDate >= :now "
            + "AND r.endedDate <= :future "
            + "AND NOT EXISTS ("
            + "    SELECT o FROM OutboxEvent o "
            + "    WHERE o.reservationId = r.id "
            + "    AND o.eventType = :event"
            + ")")
    List<Reservation> findReservationsAboutToEndNotInOutbox(
            @Param("rstate") ReservationState state,
            @Param("now") LocalDateTime currentTime,
            @Param("future") LocalDateTime futureTime,
            @Param("event") OutboxEvent.OutboxEventType eventType);

    @Query("SELECT r FROM Reservation r " + "WHERE r.state = :rstate "
            + "AND r.startedDate >= :now "
            + "AND r.startedDate <= :future "
            + "AND NOT EXISTS ("
            + "    SELECT o FROM OutboxEvent o "
            + "    WHERE o.reservationId = r.id "
            + "    AND o.eventType = :event"
            + ")")
    List<Reservation> findReservationsAboutToStartNotInOutbox(
            @Param("rstate") ReservationState state,
            @Param("now") LocalDateTime currentTime,
            @Param("future") LocalDateTime futureTime,
            @Param("event") OutboxEvent.OutboxEventType eventType);

    List<Reservation> findByStateAndEndedDateBefore(ReservationState state, LocalDateTime endedTime);

    List<Reservation> findByStateAndStartedDateBefore(ReservationState state, LocalDateTime currentTime);
}
