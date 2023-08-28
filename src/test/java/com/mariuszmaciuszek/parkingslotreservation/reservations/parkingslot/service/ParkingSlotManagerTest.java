package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ParkingSlotNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository.ParkingSlotRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingSlotManagerTest {
    private static final UUID PARKING_SLOT_ID = UUID.randomUUID();
    private static final UUID PARKING_ID = UUID.randomUUID();

    @MockBean
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    @InjectMocks
    private ParkingSlotManager parkingSlotManager;

    @BeforeEach
    void setUp() {
        reset(parkingSlotRepository);
    }

    @Test
    void findParkingSlotByIdAndParkingId() {
        doReturn(Optional.of(ParkingSlot.builder().build()))
                .when(parkingSlotRepository)
                .findParkingSlotById(PARKING_SLOT_ID, PARKING_ID);

        parkingSlotManager.findParkingSlotByIdAndParkingId(PARKING_SLOT_ID, PARKING_ID);
        verify(parkingSlotRepository).findParkingSlotById(PARKING_SLOT_ID, PARKING_ID);
    }

    @Test
    void findParkingSlotByIdAndParkingId_Should_ThrowParkingSlotNotFoundException() {
        doReturn(Optional.empty()).when(parkingSlotRepository).findParkingSlotById(PARKING_SLOT_ID, PARKING_ID);
        final ParkingSlotNotFoundException parkingSlotNotFoundException = assertThrows(
                ParkingSlotNotFoundException.class,
                () -> parkingSlotManager.findParkingSlotByIdAndParkingId(PARKING_SLOT_ID, PARKING_ID));
        verify(parkingSlotRepository).findParkingSlotById(PARKING_SLOT_ID, PARKING_ID);
        assertThat(parkingSlotNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(ParkingSlotNotFoundException.PARKING_SLOT_NOT_FOUND_MSG, PARKING_SLOT_ID));
    }

    @Test
    void findParkingSlotByIdAndParkingId_Should_ThrowAllPossibleConstraintViolationException() {
        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class,
                () -> parkingSlotManager.findParkingSlotByIdAndParkingId(null, null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(
                        tuple("findParkingSlotByIdAndParkingId.id", null),
                        tuple("findParkingSlotByIdAndParkingId.parkingId", null));
    }

    @Test
    void findAllParkingSlotsByParkingId() {
        parkingSlotManager.findAllParkingSlotsByParkingId(PARKING_ID);
        verify(parkingSlotRepository).findAllByParkingId(PARKING_ID);
    }

    @Test
    void findParkingSlotByIdAndParkingId_Should_ThrowConstraintViolationException_When_ParkingIdIsNull() {
        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class, () -> parkingSlotManager.findAllParkingSlotsByParkingId(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findAllParkingSlotsByParkingId.parkingId", null));
    }
}
