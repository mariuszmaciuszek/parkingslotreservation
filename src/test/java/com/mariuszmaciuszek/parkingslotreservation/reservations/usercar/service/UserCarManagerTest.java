package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserCarNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository.UserCarRepository;
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
class UserCarManagerTest {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID USER_CAR_ID = UUID.randomUUID();

    @MockBean
    private UserCarRepository userCarRepository;

    @Autowired
    @InjectMocks
    private UserCarManager userCarManager;

    @BeforeEach
    void setUp() {
        reset(userCarRepository);
    }

    @Test
    void findUserCar_Should_ThrowAllPossibleConstraintViolationException() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> userCarManager.findUserCar(null, null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findUserCar.id", null), tuple("findUserCar.userId", null));
    }

    @Test
    void findUserCar_Should_ThrowUserCarNotFoundException() {
        doReturn(Optional.empty()).when(userCarRepository).findUserCarByIdAndUserId(USER_CAR_ID, USER_ID);
        final UserCarNotFoundException userCarNotFoundException =
                assertThrows(UserCarNotFoundException.class, () -> userCarManager.findUserCar(USER_CAR_ID, USER_ID));
        verify(userCarRepository).findUserCarByIdAndUserId(USER_CAR_ID, USER_ID);
        assertThat(userCarNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(UserCarNotFoundException.USER_CAR_NOT_FOUND_MSG, USER_CAR_ID));
    }

    @Test
    void findUserCar() {
        doReturn(Optional.of(UserCar.builder().build()))
                .when(userCarRepository)
                .findUserCarByIdAndUserId(USER_CAR_ID, USER_ID);
        userCarManager.findUserCar(USER_CAR_ID, USER_ID);
        verify(userCarRepository).findUserCarByIdAndUserId(USER_CAR_ID, USER_ID);
    }

    @Test
    void findUserCars() {
        userCarManager.findUserCars(USER_ID);
        verify(userCarRepository).findAllUserCarsByUserId(USER_ID);
    }

    @Test
    void findUserCars_Should_ConstraintViolationException() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> userCarManager.findUserCars(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findUserCars.userId", null));
    }
}
