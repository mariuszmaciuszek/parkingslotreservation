package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CancelReservationRequest(@NotNull UUID reservationId, @NotNull UUID userId) {}
