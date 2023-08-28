package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ParkingNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Parking;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository.ParkingRepository;
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
class ParkingManagerTest {
    private static final UUID PARKING_ID = UUID.randomUUID();

    @MockBean
    private ParkingRepository parkingRepository;

    @Autowired
    @InjectMocks
    private ParkingManager parkingManager;

    @BeforeEach
    void setUp() {
        reset(parkingRepository);
    }

    @Test
    void findParkingById() {
        doReturn(Optional.of(Parking.builder().build())).when(parkingRepository).findParkingById(PARKING_ID);
        parkingManager.findParkingById(PARKING_ID);
        verify(parkingRepository).findParkingById(PARKING_ID);
    }

    @Test
    void findParkingById_Should_ThrowConstraintViolationException_When_ParkingIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> parkingManager.findParkingById(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findParkingById.id", null));
    }

    @Test
    void findParkingById_Should_ThrowParkingNotFoundException() {
        doReturn(Optional.empty()).when(parkingRepository).findParkingById(PARKING_ID);
        final ParkingNotFoundException parkingNotFoundException =
                assertThrows(ParkingNotFoundException.class, () -> parkingManager.findParkingById(PARKING_ID));
        verify(parkingRepository).findParkingById(PARKING_ID);
        assertThat(parkingNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(ParkingNotFoundException.PARKING_NOT_FOUND_MSG, PARKING_ID));
    }

    @Test
    void findAllParkings() {
        parkingManager.findAllParkings();
        verify(parkingRepository).listParkings();
    }
}
