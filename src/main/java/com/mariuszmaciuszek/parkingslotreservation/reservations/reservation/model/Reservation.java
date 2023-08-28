package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model;

import static com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState.*;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    public static final List<ReservationState> IN_PROGRESS_RESERVATION_STATES =
            List.of(ReservationState.RESERVED, ReservationState.IN_USE, ReservationState.OCCUPIED);

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID userCarId;

    @Column(nullable = false)
    private UUID parkingSlotId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationState state;

    @Column(nullable = false)
    private LocalDateTime startedDate;

    @Column(nullable = false)
    private LocalDateTime endedDate;

    private LocalDateTime occupationEndedDate;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        final LocalDateTime now = DateTimeUtils.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = DateTimeUtils.now();
    }

    public boolean canCancelReservation() {
        return (IN_PROGRESS_RESERVATION_STATES.contains(state));
    }

    public boolean isOccupied() {
        return ReservationState.OCCUPIED.equals(state);
    }

    public boolean canActivateReservation() {
        return RESERVED.equals(state);
    }

    public void cancelReservation() {
        state = CANCELED;
    }

    public void activateReservation() {
        state = IN_USE;
    }

    public void occupiedReservation() {
        state = OCCUPIED;
    }
}
