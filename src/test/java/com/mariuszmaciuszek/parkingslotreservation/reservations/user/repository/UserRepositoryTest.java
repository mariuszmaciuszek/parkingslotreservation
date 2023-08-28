package com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mariuszmaciuszek.parkingslotreservation.fixtures.UserFixtures;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.Address;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.User;
import com.neovisionaries.i18n.CountryCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FlywayDataSource
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserById() {
        final Optional<User> result = userRepository.findUserById(UserFixtures.JOHN_USER_ID);
        assertThat(result).isPresent();
        final User user = result.get();
        assertThat(user.getId()).isEqualTo(UserFixtures.JOHN_USER_ID);
        assertThat(user.getFirstName()).isNotBlank();
        assertThat(user.getLastName()).isNotBlank();
        assertThat(user.getBirthDate()).isInstanceOf(LocalDate.class);
        assertThat(user.getEmail()).isNotBlank();
        assertThat(user.getCreatedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(user.getUpdatedDate()).isInstanceOf(LocalDateTime.class);
        assertThat(user.getAddress()).isNotNull();

        final Address address = user.getAddress();
        assertThat(address.getCity()).isNotBlank();
        assertThat(address.getStreetLine1()).isNotBlank();
        assertThat(address.getStreetLine2()).isBlank();
        assertThat(address.getCountry()).isEqualTo(CountryCode.PL);
        assertThat(address.getPostcode()).isNotBlank();
    }

    @Test
    void findUserById_Should_ReturnEmptyOptional() {
        final Optional<User> result = userRepository.findUserById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }

    @Test
    void findUserById_Should_Throw_ConstraintViolationException_WhenUserIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> userRepository.findUserById(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("findUserById.id", null));
    }

    @Test
    void userExists_Should_Throw_ConstraintViolationException_WhenUserIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> userRepository.userExists(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("userExists.id", null));
    }

    @Test
    void userExists_Should_ReturnFalse_When_UserNotExists() {
        final boolean result = userRepository.userExists(UUID.randomUUID());
        assertThat(result).isFalse();
    }

    @Test
    void userExists_Should_ReturnTrue_When_UseExists() {
        final boolean result = userRepository.userExists(UserFixtures.SMITH_USER_ID);
        assertThat(result).isTrue();
    }
}
