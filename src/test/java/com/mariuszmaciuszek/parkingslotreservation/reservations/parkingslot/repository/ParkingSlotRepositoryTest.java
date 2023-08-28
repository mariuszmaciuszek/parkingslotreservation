package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mariuszmaciuszek.parkingslotreservation.fixtures.ParkingFixtures;
import com.mariuszmaciuszek.parkingslotreservation.fixtures.ParkingSlotFixtures;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotAvailability;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotSize;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingSlotRepositoryTest {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Test
    void findParkingSlotById() {
        final Optional<ParkingSlot> result =
                parkingSlotRepository.findParkingSlotById(ParkingSlotFixtures.PARKING_SLOT_SMALL_NOT_AVAILABLE_ID);
        assertThat(result).isPresent();

        final ParkingSlot parkingSlot = result.get();
        assertThat(parkingSlot.getId()).isEqualTo(ParkingSlotFixtures.PARKING_SLOT_SMALL_NOT_AVAILABLE_ID);
        assertThat(parkingSlot.getParkingId()).isInstanceOf(UUID.class);
        assertThat(parkingSlot.getAvailability()).isEqualTo(ParkingSlotAvailability.NOT_AVAILABLE);
        assertThat(parkingSlot.getSize()).isEqualTo(ParkingSlotSize.SMALL);
        assertThat(parkingSlot.getCreatedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(parkingSlot.getUpdatedDate()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    void findParkingSlotById_Should_ReturnEmptyOptional() {
        final Optional<ParkingSlot> result = parkingSlotRepository.findParkingSlotById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void findParkingSlotById_Should_Throw_ConstraintViolationException_When_ParkingSlotIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> parkingSlotRepository.findParkingSlotById(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findParkingSlotById.id", null));
    }

    @Test
    void parkingSlotExists_Should_ReturnTrue_When_Exists() {
        boolean result = parkingSlotRepository.parkingSlotExists(ParkingSlotFixtures.PARKING_SLOT_LARGE_AVAILABLE_ID);
        assertThat(result).isTrue();
    }

    @Test
    void parkingSlotExists_Should_ReturnFalse_When_NotExists() {
        boolean result = parkingSlotRepository.parkingSlotExists(UUID.randomUUID());
        assertThat(result).isFalse();
    }

    @Test
    void parkingSlotExists_Should_Throw_ConstraintViolationException_When_ParkingSlotIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> parkingSlotRepository.parkingSlotExists(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("parkingSlotExists.id", null));
    }

    @Test
    @FlywayTest
    void findAvailableParkingSlot() {
        final Optional<ParkingSlot> result = parkingSlotRepository.findAvailableParkingSlot(
                ParkingFixtures.PARKING_OUTDOOR_ID, ParkingSlotSize.LARGE);
        assertThat(result).isPresent();
        final ParkingSlot parkingSlot = result.get();
        assertThat(parkingSlot.getSize()).isEqualTo(ParkingSlotSize.LARGE);
        assertThat(parkingSlot.getParkingId()).isEqualTo(ParkingFixtures.PARKING_OUTDOOR_ID);
    }

    @Test
    void findAvailableParkingSlot_Should_ReturnEmptyResult() {
        final Optional<ParkingSlot> result = parkingSlotRepository.findAvailableParkingSlot(
                ParkingFixtures.PARKING_INDOOR_ID, ParkingSlotSize.LARGE);
        assertThat(result).isEmpty();
    }

    @Test
    void
            findAvailableParkingSlot_Should_Throw_ConstraintViolationException_When_ParkingIdIsNull_Or_ParkingSlotSizeIsNull() {
        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class, () -> parkingSlotRepository.findAvailableParkingSlot(null, null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .containsExactlyInAnyOrder(
                        tuple("findAvailableParkingSlot.parkingId", null),
                        tuple("findAvailableParkingSlot.parkingSlotSize", null));
    }

    @Test
    void save_Should_Throw_ConstraintViolationException_When_ParkingSlotIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> parkingSlotRepository.save(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("save.parkingSlot", null));
    }
}
