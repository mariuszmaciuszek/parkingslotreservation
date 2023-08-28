package com.mariuszmaciuszek.parkingslotreservation.reservations.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.User;
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
class UserManagerTest {
    private static final UUID USER_ID = UUID.randomUUID();

    @MockBean
    private UserRepository repository;

    @Autowired
    @InjectMocks
    private UserManager userManager;

    @BeforeEach
    void setUp() {
        reset(repository);
    }

    @Test
    void getUserByUserId_Should_ThrowConstraintViolationException_When_UserIdIsNull() {
        final ConstraintViolationException constraintViolationException =
                assertThrows(ConstraintViolationException.class, () -> userManager.getUserByUserId(null));
        assertThat(constraintViolationException.getConstraintViolations())
                .hasSize(1)
                .extracting(
                        constraintViolation ->
                                constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getInvalidValue)
                .contains(tuple("getUserByUserId.id", null));
    }

    @Test
    void getUserByUserId() {
        doReturn(Optional.of(User.builder().build())).when(repository).findUserById(USER_ID);
        userManager.getUserByUserId(USER_ID);
        verify(repository).findUserById(USER_ID);
    }

    @Test
    void getUserByUserId_Should_ThrowUserNotFoundException() {
        doReturn(Optional.empty()).when(repository).findUserById(USER_ID);
        final UserNotFoundException userNotFoundException =
                assertThrows(UserNotFoundException.class, () -> userManager.getUserByUserId(USER_ID));
        verify(repository).findUserById(USER_ID);
        assertThat(userNotFoundException)
                .isNotNull()
                .extracting(Throwable::getMessage)
                .isEqualTo(String.format(UserNotFoundException.USER_NOT_FOUND_MSG, USER_ID));
    }

    @Test
    void findAllUsers() {
        userManager.findAllUsers();
        verify(repository).findAllUsers();
    }
}
