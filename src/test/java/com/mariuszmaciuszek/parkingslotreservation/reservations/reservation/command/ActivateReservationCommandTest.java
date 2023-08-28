package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationCannotBeActivatedException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository.UserRepository;
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
class ActivateReservationCommandTest {
    private static final UUID RESERVATION_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    @InjectMocks
    private ActivateReservationCommand activateReservationCommand;

    @BeforeEach
    void setUp() {
        reset(userRepository, reservationRepository);
    }

    @Test
    void validateActivateCommandDescriptor_Should_ThrowAllPossible_ConstraintViolationExceptions() {
        final ActivateReservationCommand.ActivateCommandDescriptor command =
                ActivateReservationCommand.ActivateCommandDescriptor.builder().build();
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> activateReservationCommand.run(command));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(
                        tuple("run.commandDescriptor.userId", null),
                        tuple("run.commandDescriptor.reservationId", null));
    }

    @Test
    void run_Should_Throw_ConstraintViolationException_When_CommandDescriptorIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> activateReservationCommand.run(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("run.commandDescriptor", null));
    }

    @Test
    void run_Should_ThrowUserNotFoundException_When_UserNotFound() {
        final ActivateReservationCommand.ActivateCommandDescriptor command =
                commandBuilder().build();

        doReturn(false).when(userRepository).userExists(USER_ID);

        final UserNotFoundException userNotFoundException =
                assertThrows(UserNotFoundException.class, () -> activateReservationCommand.run(command));
        assertThat(userNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(UserNotFoundException.USER_NOT_FOUND_MSG, USER_ID));
    }

    @Test
    void run_Should_ThrowReservationFoundException_When_ReservationNotFound() {
        final ActivateReservationCommand.ActivateCommandDescriptor command =
                commandBuilder().build();

        doReturn(true).when(userRepository).userExists(USER_ID);

        final ReservationNotFoundException reservationNotFoundException =
                assertThrows(ReservationNotFoundException.class, () -> activateReservationCommand.run(command));
        assertThat(reservationNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(ReservationNotFoundException.CANNOT_CANCEL_MSG, RESERVATION_ID));
    }

    @Test
    void run_Should_ThrowReservationCannotBeActivatedException_When_ReservationCanNotBeActivated() {
        final Reservation reservation =
                Reservation.builder().state(ReservationState.CANCELED).build();
        final ActivateReservationCommand.ActivateCommandDescriptor command =
                commandBuilder().build();

        doReturn(Optional.of(reservation))
                .when(reservationRepository)
                .findReservationByIdAndUserId(RESERVATION_ID, USER_ID);
        doReturn(true).when(userRepository).userExists(USER_ID);

        final ReservationCannotBeActivatedException reservationCannotBeActivatedException = assertThrows(
                ReservationCannotBeActivatedException.class, () -> activateReservationCommand.run(command));
        assertThat(reservationCannotBeActivatedException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(ReservationCannotBeActivatedException.CANNOT_ACTIVATE_MSG, RESERVATION_ID));
    }

    @Test
    void run_Should_ActivateReservation() {
        final Reservation reservation = Reservation.builder()
                .id(RESERVATION_ID)
                .state(ReservationState.RESERVED)
                .build();
        final ActivateReservationCommand.ActivateCommandDescriptor command =
                commandBuilder().build();

        doReturn(Optional.of(reservation))
                .when(reservationRepository)
                .findReservationByIdAndUserId(RESERVATION_ID, USER_ID);
        doReturn(true).when(userRepository).userExists(USER_ID);

        activateReservationCommand.run(command);

        reservation.activateReservation();
        verify(reservationRepository).save(reservation);
    }

    private static ActivateReservationCommand.ActivateCommandDescriptor.ActivateCommandDescriptorBuilder
            commandBuilder() {
        return ActivateReservationCommand.ActivateCommandDescriptor.builder()
                .reservationId(RESERVATION_ID)
                .userId(USER_ID);
    }
}
