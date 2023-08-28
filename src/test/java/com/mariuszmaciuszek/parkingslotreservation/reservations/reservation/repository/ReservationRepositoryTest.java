package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mariuszmaciuszek.parkingslotreservation.fixtures.ReservationFixtures;
import com.mariuszmaciuszek.parkingslotreservation.fixtures.UserCarFixtures;
import com.mariuszmaciuszek.parkingslotreservation.fixtures.UserFixtures;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void findReservationByIdAndUserId() {
        final Optional<Reservation> result = reservationRepository.findReservationByIdAndUserId(
                ReservationFixtures.RESERVATION_RESERVED_JOHN_LARGE_CAR, UserFixtures.JOHN_USER_ID);
        assertThat(result).isPresent();

        final Reservation reservation = result.get();
        assertThat(reservation.getUserId()).isEqualTo(UserFixtures.JOHN_USER_ID);
        assertThat(reservation.getUserCarId()).isEqualTo(UserCarFixtures.USER_CAR_ID_LARGE_CAR_FOR_JOHN);
        assertThat(reservation.getState()).isEqualTo(ReservationState.RESERVED);
        assertThat(reservation.getParkingSlotId()).isInstanceOf(UUID.class);
        assertThat(reservation.getStartedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(reservation.getEndedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(reservation.getOccupationEndedDate()).isNull();
        assertThat(reservation.getCreatedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(reservation.getUpdatedDate()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    void findReservationByIdAndUserId_Should_Throw_ConstraintViolationException_When_IdIsNull_Or_UserCarIdIsNull() {
        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class,
                () -> reservationRepository.findReservationByIdAndUserId(null, null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .containsExactlyInAnyOrder(
                        tuple("findReservationByIdAndUserId.id", null),
                        tuple("findReservationByIdAndUserId.userId", null));
    }

    @Test
    void save_Should_Throw_ConstraintViolationException_When_ReservationNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> reservationRepository.save(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("save.reservation", null));
    }

    @Test
    void hasUserCarIdActiveReservation_Should_ReturnTrue() {
        final boolean result = reservationRepository.hasUserCarIdActiveReservation(
                UserCarFixtures.USER_CAR_ID_LARGE_CAR_FOR_JOHN, Reservation.IN_PROGRESS_RESERVATION_STATES);
        assertThat(result).isTrue();
    }

    @Test
    void hasUserCarIdActiveReservation_Should_ReturnFalse() {
        final boolean result = reservationRepository.hasUserCarIdActiveReservation(
                UserCarFixtures.USER_CAR_ID_MEDIUM_FOR_MARK, Reservation.IN_PROGRESS_RESERVATION_STATES);
        assertThat(result).isFalse();
    }

    @Test
    void hasUserCarIdActiveReservation_ConstraintViolationException_When_UserCarIdIsNull_Or_StatesAreEmpty() {
        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class,
                () -> reservationRepository.hasUserCarIdActiveReservation(null, null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(2)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(
                        tuple("hasUserCarIdActiveReservation.userCarId", null),
                        tuple("hasUserCarIdActiveReservation.states", null));
    }

    @Test
    void findAllReservationsByUserId() {
        final List<Reservation> result = reservationRepository.findAllReservationsByUserId(UserFixtures.JOHN_USER_ID);
        assertThat(result)
                .hasSize(2)
                .extracting(Reservation::getUserId)
                .containsOnly(UserFixtures.JOHN_USER_ID, UserFixtures.JOHN_USER_ID);
    }

    @Test
    void findAllReservationsByUserId_ConstraintViolationException_When_UserIdIsNull() {
        final ConstraintViolationException constraintViolationException = assertThrows(
                ConstraintViolationException.class, () -> reservationRepository.findAllReservationsByUserId(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findAllReservationsByUserId.userId", null));
    }
}
