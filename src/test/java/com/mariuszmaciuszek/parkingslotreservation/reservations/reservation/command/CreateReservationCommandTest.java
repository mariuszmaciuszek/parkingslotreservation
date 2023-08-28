package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.*;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository.ParkingRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotAvailability;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotSize;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository.ParkingSlotRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository.UserRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.CarSize;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository.UserCarRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateReservationCommandTest {
    private static final UUID RESERVATION_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID PARKING_ID = UUID.randomUUID();
    private static final UUID USER_CAR_ID = UUID.randomUUID();
    private static final LocalDateTime START_DATE = DateTimeUtils.now().plusHours(4);
    private static final LocalDateTime END_DATE = DateTimeUtils.now().plusHours(8);

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private UserCarRepository userCarRepository;

    @MockBean
    private ParkingRepository parkingRepository;

    @MockBean
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    @InjectMocks
    private CreateReservationCommand createReservationCommand;

    @Captor
    private ArgumentCaptor<Reservation> captor;

    @BeforeEach
    void beforeEach() {
        reset(userRepository, reservationRepository, userCarRepository, parkingRepository, parkingSlotRepository);
    }

    @Test
    void validateCreateReservationCommandDescriptor_Should_ThrowAllPossible_ConstraintViolationExceptions() {
        final CreateReservationCommand.CreateReservationCommandDescriptor command =
                CreateReservationCommand.CreateReservationCommandDescriptor.builder()
                        .build();
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> createReservationCommand.run(command));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(5)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(
                        tuple("run.commandDescriptor.userCarId", null),
                        tuple("run.commandDescriptor.userId", null),
                        tuple("run.commandDescriptor.parkingId", null),
                        tuple("run.commandDescriptor.startDate", null),
                        tuple("run.commandDescriptor.endDate", null));
    }

    @Test
    void run_Should_Throw_ConstraintViolationException_When_CommandDescriptorIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> createReservationCommand.run(null));
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
        final CreateReservationCommand.CreateReservationCommandDescriptor command =
                commandBuilder().build();

        doReturn(false).when(userRepository).userExists(USER_ID);

        final UserNotFoundException userNotFoundException =
                assertThrows(UserNotFoundException.class, () -> createReservationCommand.run(command));
        assertThat(userNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(UserNotFoundException.USER_NOT_FOUND_MSG, USER_ID));
    }

    @Test
    void run_Should_ThrowParkingFoundException_When_UserNotFound() {
        final CreateReservationCommand.CreateReservationCommandDescriptor command =
                commandBuilder().build();

        doReturn(true).when(userRepository).userExists(USER_ID);
        doReturn(false).when(parkingRepository).parkingExists(PARKING_ID);

        final ParkingNotFoundException parkingNotFoundException =
                assertThrows(ParkingNotFoundException.class, () -> createReservationCommand.run(command));
        assertThat(parkingNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(ParkingNotFoundException.PARKING_NOT_FOUND_MSG, PARKING_ID));
    }

    @Test
    void run_Should_ThrowReservationPeriodMismatchException_When_EndDateIsBeforeStartDate() {
        final CreateReservationCommand.CreateReservationCommandDescriptor command = commandBuilder()
                .endDate(DateTimeUtils.now().plusHours(2))
                .startDate(DateTimeUtils.now().plusHours(3))
                .build();

        doReturn(true).when(userRepository).userExists(USER_ID);
        doReturn(true).when(parkingRepository).parkingExists(PARKING_ID);

        final ReservationPeriodMismatchException reservationNotFoundException =
                assertThrows(ReservationPeriodMismatchException.class, () -> createReservationCommand.run(command));
        assertThat(reservationNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(ReservationPeriodMismatchException.MSG);
    }

    @Test
    void run_Should_ThrowCarHasActiveReservationException_When_CarHasActiveReservation() {
        final CreateReservationCommand.CreateReservationCommandDescriptor command =
                commandBuilder().build();

        doReturn(true).when(userRepository).userExists(USER_ID);
        doReturn(true).when(parkingRepository).parkingExists(PARKING_ID);
        doReturn(true)
                .when(reservationRepository)
                .hasUserCarIdActiveReservation(USER_CAR_ID, Reservation.IN_PROGRESS_RESERVATION_STATES);

        final ReservationCreationException.CarHasActiveReservationException carHasActiveReservationException =
                assertThrows(
                        ReservationCreationException.CarHasActiveReservationException.class,
                        () -> createReservationCommand.run(command));
        assertThat(carHasActiveReservationException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(
                        ReservationCreationException.CarHasActiveReservationException.CAR_HAS_ACTIVE_RESERVATION_MSG,
                        USER_CAR_ID));
    }

    @Test
    void run_Should_ThrowUserCarNotFoundException_When_CarWasNotFound() {
        final CreateReservationCommand.CreateReservationCommandDescriptor command =
                commandBuilder().build();
        //        final UserCar userCar =
        // UserCar.builder().userId(USER_ID).id(USER_CAR_ID).carSize(CarSize.COMPACT).build();

        doReturn(true).when(userRepository).userExists(USER_ID);
        doReturn(true).when(parkingRepository).parkingExists(PARKING_ID);
        doReturn(false)
                .when(reservationRepository)
                .hasUserCarIdActiveReservation(USER_CAR_ID, Reservation.IN_PROGRESS_RESERVATION_STATES);
        doReturn(Optional.empty()).when(userCarRepository).findUserCarById(USER_CAR_ID);

        final UserCarNotFoundException userCarNotFoundException =
                assertThrows(UserCarNotFoundException.class, () -> createReservationCommand.run(command));
        assertThat(userCarNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(UserCarNotFoundException.USER_CAR_NOT_FOUND_MSG, USER_CAR_ID));
    }

    @ParameterizedTest
    @EnumSource(value = CarSize.class)
    void run_Should_ThrowParkingSlotNotAvailableException_When_AvailableParkingSlotNotFoundForCompactCarSize(
            CarSize carSize) {
        final CreateReservationCommand.CreateReservationCommandDescriptor command =
                commandBuilder().build();
        final UserCar userCar = UserCar.builder()
                .userId(USER_ID)
                .id(USER_CAR_ID)
                .carSize(carSize)
                .build();

        doReturn(true).when(userRepository).userExists(USER_ID);
        doReturn(true).when(parkingRepository).parkingExists(PARKING_ID);
        doReturn(false)
                .when(reservationRepository)
                .hasUserCarIdActiveReservation(USER_CAR_ID, Reservation.IN_PROGRESS_RESERVATION_STATES);
        doReturn(Optional.of(userCar)).when(userCarRepository).findUserCarById(USER_CAR_ID);

        doReturn(Optional.empty())
                .when(parkingSlotRepository)
                .findAvailableParkingSlot(PARKING_ID, ParkingSlotSize.SMALL);
        doReturn(Optional.empty())
                .when(parkingSlotRepository)
                .findAvailableParkingSlot(PARKING_ID, ParkingSlotSize.MEDIUM);
        doReturn(Optional.empty())
                .when(parkingSlotRepository)
                .findAvailableParkingSlot(PARKING_ID, ParkingSlotSize.LARGE);

        final ParkingSlotNotAvailableException parkingSlotNotAvailableException =
                assertThrows(ParkingSlotNotAvailableException.class, () -> createReservationCommand.run(command));
        assertThat(parkingSlotNotAvailableException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(ParkingSlotNotAvailableException.PARKING_SLOT_NOT_AVAILABLE_MSG, PARKING_ID));
    }

    @Test
    void run_Should_CreateReservation() {
        final UUID parkingSlotId = UUID.randomUUID();
        final CreateReservationCommand.CreateReservationCommandDescriptor command =
                commandBuilder().build();
        final UserCar userCar = UserCar.builder()
                .userId(USER_ID)
                .id(USER_CAR_ID)
                .carSize(CarSize.LARGE)
                .build();
        final ParkingSlot parkingSlot = ParkingSlot.builder()
                .parkingId(PARKING_ID)
                .id(parkingSlotId)
                .availability(ParkingSlotAvailability.AVAILABLE)
                .size(ParkingSlotSize.LARGE)
                .build();

        doReturn(true).when(userRepository).userExists(USER_ID);
        doReturn(true).when(parkingRepository).parkingExists(PARKING_ID);
        doReturn(false)
                .when(reservationRepository)
                .hasUserCarIdActiveReservation(USER_CAR_ID, Reservation.IN_PROGRESS_RESERVATION_STATES);
        doReturn(Optional.of(userCar)).when(userCarRepository).findUserCarById(USER_CAR_ID);
        doReturn(Optional.of(parkingSlot))
                .when(parkingSlotRepository)
                .findAvailableParkingSlot(PARKING_ID, ParkingSlotSize.LARGE);

        doReturn(parkingSlot).when(parkingSlotRepository).save(parkingSlot);
        doReturn(Reservation.builder().id(RESERVATION_ID).build())
                .when(reservationRepository)
                .save(any(Reservation.class));

        createReservationCommand.run(command);
        verify(reservationRepository).save(captor.capture());

        final Reservation reservation = captor.getValue();
        assertThat(reservation).isNotNull();
        assertThat(reservation.getId()).isEqualTo(command.getReservationId());
        assertThat(reservation.getUserCarId()).isEqualTo(command.getUserCarId());
        assertThat(reservation.getUserId()).isEqualTo(command.getUserId());
        assertThat(reservation.getParkingSlotId()).isEqualTo(parkingSlotId);
        assertThat(reservation.getOccupationEndedDate()).isNull();
        assertThat(reservation.getStartedDate()).isEqualTo(START_DATE);
        assertThat(reservation.getEndedDate()).isEqualTo(END_DATE);
        assertThat(reservation.getState()).isEqualTo(ReservationState.RESERVED);
    }

    private static CreateReservationCommand.CreateReservationCommandDescriptor.CreateReservationCommandDescriptorBuilder
            commandBuilder() {
        return CreateReservationCommand.CreateReservationCommandDescriptor.builder()
                .reservationId(RESERVATION_ID)
                .userId(USER_ID)
                .parkingId(PARKING_ID)
                .userCarId(USER_CAR_ID)
                .startDate(START_DATE)
                .endDate(END_DATE);
    }
}
