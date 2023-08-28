package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto;

import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID userId,
        UUID userCarId,
        UUID parkingSlotId,
        ReservationState state,
        LocalDateTime startedDate,
        LocalDateTime endedDate,
        LocalDateTime createdDate) {}
