package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mariuszmaciuszek.parkingslotreservation.fixtures.ParkingFixtures;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Address;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Parking;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.ParkingType;
import com.neovisionaries.i18n.CountryCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingRepositoryTest {

    @Autowired
    private ParkingRepository parkingRepository;

    @Test
    void findParkingById() {
        final Optional<Parking> result = parkingRepository.findParkingById(ParkingFixtures.PARKING_INDOOR_ID);
        assertThat(result).isPresent();

        final Parking parking = result.get();
        assertThat(parking.getId()).isEqualTo(ParkingFixtures.PARKING_INDOOR_ID);
        assertThat(parking.getName()).isNotBlank();
        assertThat(parking.getType()).isEqualTo(ParkingType.INDOOR);
        assertThat(parking.getCreatedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(parking.getUpdatedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(parking.getAddress()).isNotNull();

        final Address address = parking.getAddress();
        assertThat(address.getCity()).isNotBlank();
        assertThat(address.getStreetLine1()).isNotBlank();
        assertThat(address.getStreetLine2()).isNotBlank();
        assertThat(address.getCountry()).isEqualTo(CountryCode.PL);
        assertThat(address.getPostcode()).isNotBlank();
    }

    @Test
    void findParkingById_Should_ReturnEmptyOptional() {
        final Optional<Parking> result = parkingRepository.findParkingById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void findParkingById_Should_Throw_ConstraintViolationException_When_ParkingIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> parkingRepository.findParkingById(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findParkingById.id", null));
    }

    @Test
    void parkingExists_Should_ReturnFalse_When_NotExists() {
        boolean result = parkingRepository.parkingExists(UUID.randomUUID());
        assertThat(result).isFalse();
    }

    @Test
    void parkingExists_Should_ReturnTrue_When_Exists() {
        boolean result = parkingRepository.parkingExists(ParkingFixtures.PARKING_INDOOR_ID);
        assertThat(result).isTrue();
    }

    @Test
    void parkingExists_Should_Throw_ConstraintViolationException_When_ParkingIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> parkingRepository.parkingExists(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("parkingExists.id", null));
    }

    @Test
    void listParkings() {
        final List<Parking> parkings = parkingRepository.listParkings();
        assertThat(parkings)
                .hasSize(2)
                .extracting(Parking::getId)
                .containsExactlyInAnyOrder(ParkingFixtures.PARKING_INDOOR_ID, ParkingFixtures.PARKING_OUTDOOR_ID);
    }
}
