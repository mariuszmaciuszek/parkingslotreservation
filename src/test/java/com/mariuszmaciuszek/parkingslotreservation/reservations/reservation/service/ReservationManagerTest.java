package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationCannotBeCanceledException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationCreationException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command.ActivateReservationCommand;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command.CancelReservationCommand;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command.CreateReservationCommand;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.ActivateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CancelReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CreateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationManagerTest {
    private static final UUID RESERVATION_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID PARKING_ID = UUID.randomUUID();
    private static final UUID USER_CAR_ID = UUID.randomUUID();
    private static final LocalDateTime START_DATE = DateTimeUtils.now().plusHours(4);
    private static final LocalDateTime END_DATE = DateTimeUtils.now().plusHours(8);
    private static final Optional<Reservation> RESERVATION_OPTIONAL =
            Optional.of(Reservation.builder().id(RESERVATION_ID).build());

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private CreateReservationCommand createReservationCommand;

    @MockBean
    private CancelReservationCommand cancelReservationCommand;

    @MockBean
    private ActivateReservationCommand activateReservationCommand;

    @Autowired
    @InjectMocks
    private ReservationManager reservationManager;

    @BeforeEach
    void setUp() {
        reset(reservationRepository, createReservationCommand, cancelReservationCommand, activateReservationCommand);
    }

    @Test
    void validateCreateReservationRequest_Should_ThrowAllPossibleConstraintViolationExceptions() {
        final CreateReservationRequest createReservationRequest =
                CreateReservationRequest.builder().build();

        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class,
                () -> reservationManager.createReservation(createReservationRequest));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(5)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(
                        tuple("createReservation.reservationRequest.userId", null),
                        tuple("createReservation.reservationRequest.userCarId", null),
                        tuple("createReservation.reservationRequest.parkingId", null),
                        tuple("createReservation.reservationRequest.startTime", null),
                        tuple("createReservation.reservationRequest.endTime", null));
    }

    @Test
    void createReservation_Should_ThrowConstraintViolationExceptions_When_RequestIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> reservationManager.createReservation(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("createReservation.reservationRequest", null));
    }

    @Test
    void createReservation_ShouldThrow_ReservationCreationInternalException() {
        final CreateReservationRequest createReservationRequest =
                buildCreateReservationRequestBuilder().build();

        doReturn(Optional.empty())
                .when(createReservationCommand)
                .run(any(CreateReservationCommand.CreateReservationCommandDescriptor.class));

        final ReservationCreationException.ReservationCreationInternalException reservationCreationInternalException =
                assertThrows(
                        ReservationCreationException.ReservationCreationInternalException.class,
                        () -> reservationManager.createReservation(createReservationRequest));
        assertThat(reservationCreationInternalException)
                .extracting(Throwable::getMessage)
                .isEqualTo(
                        ReservationCreationException.ReservationCreationInternalException.RESERVATION_NOT_CREATED_MSG);
    }

    @Test
    void createReservation() {
        final CreateReservationRequest createReservationRequest =
                buildCreateReservationRequestBuilder().build();

        doReturn(RESERVATION_OPTIONAL)
                .when(createReservationCommand)
                .run(any(CreateReservationCommand.CreateReservationCommandDescriptor.class));

        reservationManager.createReservation(createReservationRequest);
        verify(createReservationCommand).run(any(CreateReservationCommand.CreateReservationCommandDescriptor.class));
    }

    @Test
    void cancelReservation_Should_ThrowConstraintViolationExceptions_When_RequestIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> reservationManager.cancelReservation(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("cancelReservation.cancelRequest", null));
    }

    @Test
    void validateCancelReservationRequest_Should_ThrowAllPossibleConstraintViolationExceptions() {
        final CancelReservationRequest cancelReservationRequest =
                CancelReservationRequest.builder().build();

        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class,
                () -> reservationManager.cancelReservation(cancelReservationRequest));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(
                        tuple("cancelReservation.cancelRequest.reservationId", null),
                        tuple("cancelReservation.cancelRequest.userId", null));
    }

    @Test
    void cancelReservation() {
        final CancelReservationRequest cancelReservationRequest = getCancelReservationRequest();

        doReturn(RESERVATION_OPTIONAL)
                .when(cancelReservationCommand)
                .run(any(CancelReservationCommand.CancelReservationCommandDescriptor.class));

        reservationManager.cancelReservation(cancelReservationRequest);

        verify(cancelReservationCommand).run(any(CancelReservationCommand.CancelReservationCommandDescriptor.class));
    }

    @Test
    void createReservation_ShouldThrow_ReservationCannotBeCanceledException() {
        final CancelReservationRequest cancelReservationRequest = getCancelReservationRequest();

        doReturn(Optional.empty())
                .when(cancelReservationCommand)
                .run(any(CancelReservationCommand.CancelReservationCommandDescriptor.class));

        final ReservationCannotBeCanceledException reservationCreationInternalException = assertThrows(
                ReservationCannotBeCanceledException.class,
                () -> reservationManager.cancelReservation(cancelReservationRequest));
        assertThat(reservationCreationInternalException)
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(ReservationCannotBeCanceledException.CANNOT_CANCEL_MSG, RESERVATION_ID));
    }

    @Test
    void activateReservation_Should_ThrowConstraintViolationExceptions_When_RequestIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> reservationManager.activateReservation(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("activateReservation.activateReservationRequest", null));
    }

    @Test
    void validateActivateReservationRequest_Should_ThrowAllPossibleConstraintViolationExceptions() {
        final ActivateReservationRequest activateReservationRequest =
                ActivateReservationRequest.builder().build();

        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class,
                () -> reservationManager.activateReservation(activateReservationRequest));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(
                        tuple("activateReservation.activateReservationRequest.reservationId", null),
                        tuple("activateReservation.activateReservationRequest.userId", null));
    }

    @Test
    void activateReservation() {
        final ActivateReservationRequest activateReservationRequest = ActivateReservationRequest.builder()
                .reservationId(RESERVATION_ID)
                .userId(USER_ID)
                .build();

        doReturn(RESERVATION_OPTIONAL)
                .when(activateReservationCommand)
                .run(any(ActivateReservationCommand.ActivateCommandDescriptor.class));

        reservationManager.activateReservation(activateReservationRequest);
        verify(activateReservationCommand).run(any(ActivateReservationCommand.ActivateCommandDescriptor.class));
    }

    @Test
    void getReservation_Should_ThrowAllPossibleConstraintViolationExceptions() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> reservationManager.getReservation(null, null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("getReservation.id", null), tuple("getReservation.userId", null));
    }

    @Test
    void getReservation_Should_ThrowReservationNotFoundException() {
        final ReservationNotFoundException reservationNotFoundException = assertThrows(
                ReservationNotFoundException.class, () -> reservationManager.getReservation(RESERVATION_ID, USER_ID));
        assertThat(reservationNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(ReservationNotFoundException.CANNOT_CANCEL_MSG, RESERVATION_ID));
    }

    @Test
    void getReservation() {
        doReturn(RESERVATION_OPTIONAL)
                .when(reservationRepository)
                .findReservationByIdAndUserId(RESERVATION_ID, USER_ID);

        final Reservation reservationResponse = reservationManager.getReservation(RESERVATION_ID, USER_ID);
        assertThat(reservationResponse).isEqualTo(RESERVATION_OPTIONAL.get());
    }

    @Test
    void listAllReservationsByUserId_Should_ThrowConstraintViolationException_When_UserIdIsNull() {
        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class, () -> reservationManager.listAllReservationsByUserId(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("listAllReservationsByUserId.userId", null));
    }

    @Test
    void listAllReservationsByUserId() {
        reservationManager.listAllReservationsByUserId(USER_ID);

        verify(reservationRepository).findAllReservationsByUserId(USER_ID);
    }

    private static CreateReservationRequest.CreateReservationRequestBuilder buildCreateReservationRequestBuilder() {
        return CreateReservationRequest.builder()
                .userId(USER_ID)
                .userCarId(USER_CAR_ID)
                .parkingId(PARKING_ID)
                .endTime(END_DATE)
                .startTime(START_DATE);
    }

    private static CancelReservationRequest getCancelReservationRequest() {
        return CancelReservationRequest.builder()
                .reservationId(RESERVATION_ID)
                .userId(USER_ID)
                .build();
    }
}
