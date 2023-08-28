package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mariuszmaciuszek.parkingslotreservation.fixtures.UserCarFixtures;
import com.mariuszmaciuszek.parkingslotreservation.fixtures.UserFixtures;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.CarSize;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
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
class UserCarRepositoryTest {

    @Autowired
    private UserCarRepository userCarRepository;

    @Test
    void findUserCarById() {
        final Optional<UserCar> result =
                userCarRepository.findUserCarById(UserCarFixtures.USER_CAR_ID_LARGE_CAR_FOR_JOHN);
        assertThat(result).isPresent();

        final UserCar userCar = result.get();
        assertThat(userCar.getUserId()).isEqualTo(UserFixtures.JOHN_USER_ID);
        assertThat(userCar.getId()).isEqualTo(UserCarFixtures.USER_CAR_ID_LARGE_CAR_FOR_JOHN);
        assertThat(userCar.getCarSize()).isEqualTo(CarSize.LARGE);
        assertThat(userCar.getPlate()).isNotBlank();
        assertThat(userCar.getCreatedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(userCar.getUpdatedDate()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    void findUserCarById_Should_ReturnEmptyResponse() {
        final Optional<UserCar> result = userCarRepository.findUserCarById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void findUserCarById_Should_Throw_ConstraintViolationException_WhenUserIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> userCarRepository.findUserCarById(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findUserCarById.id", null));
    }

    @Test
    void findAllUserCarsByUserId_Should_Throw_ConstraintViolationException_WhenUserIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> userCarRepository.findAllUserCarsByUserId(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findAllUserCarsByUserId.userId", null));
    }

    @Test
    void findAllUserCarsByUserId() {
        final List<UserCar> userCars = userCarRepository.findAllUserCarsByUserId(UserFixtures.MARK_USER_ID);
        assertThat(userCars).hasSize(1).allMatch(car -> car.getUserId().equals(UserFixtures.MARK_USER_ID));
    }
}
