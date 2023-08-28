package com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Slf4j
@Validated
public class UserRepository {
    private final UserJpaRepository repository;

    public Optional<User> findUserById(@NotNull UUID id) {
        log.debug("getUser({})", id);
        return repository.findById(id);
    }

    public boolean userExists(@NotNull UUID id) {
        log.debug("userExists({})", id);
        return repository.existsById(id);
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public User save(User user) {
        return repository.save(user);
    }
}
