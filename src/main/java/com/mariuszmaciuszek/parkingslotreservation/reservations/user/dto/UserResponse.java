package com.mariuszmaciuszek.parkingslotreservation.reservations.user.dto;

import com.neovisionaries.i18n.CountryCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String email,
        Address address,
        LocalDateTime createdDate,
        LocalDateTime updatedDate) {
    @Builder
    public record Address(String streetLine1, String streetLine2, String city, CountryCode country, String postcode) {}
}
