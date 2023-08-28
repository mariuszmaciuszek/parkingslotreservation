package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.dto;

import com.neovisionaries.i18n.CountryCode;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ParkingResponse(
        UUID id, String name, ParkingType type, Address address, LocalDateTime createdDate, LocalDateTime updatedDate) {
    @Builder
    public record Address(String streetLine1, String streetLine2, String city, CountryCode country, String postcode) {}

    public enum ParkingType {
        INDOOR,
        OUTDOOR
    }
}
