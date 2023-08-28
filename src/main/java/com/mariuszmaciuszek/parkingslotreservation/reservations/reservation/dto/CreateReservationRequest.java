package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.validation.constraint.DateNotFromPast;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateReservationRequest(
        @NotNull UUID userId,
        @NotNull UUID userCarId,
        @NotNull UUID parkingId,
        @DateNotFromPast LocalDateTime startTime,
        @DateNotFromPast LocalDateTime endTime) {}
