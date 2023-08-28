package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ParkingSlotResponse(
        UUID id,
        UUID parkingId,
        ParkingSlotSize size,
        ParkingSlotAvailability availability,
        LocalDateTime createdDate,
        LocalDateTime updatedDate) {
    public enum ParkingSlotSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    public enum ParkingSlotAvailability {
        AVAILABLE,
        NOT_AVAILABLE
    }
}
