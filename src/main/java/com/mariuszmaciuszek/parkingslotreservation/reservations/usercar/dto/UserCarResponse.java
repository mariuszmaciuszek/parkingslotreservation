package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserCarResponse(
        UUID id, UUID userId, String plate, CarSize carSize, LocalDateTime createdDate, LocalDateTime updatedDate) {
    public enum CarSize {
        COMPACT,
        MEDIUM,
        LARGE
    }
}
