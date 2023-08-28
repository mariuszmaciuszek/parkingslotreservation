package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
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
public class UserCarRepository {
    private final UserCarsJpaRepository repository;

    public Optional<UserCar> findUserCarById(@NotNull UUID id) {
        log.debug("findUserCarById({})", id);
        return repository.findById(id);
    }

    public Optional<UserCar> findUserCarByIdAndUserId(@NotNull UUID id, @NotNull UUID userId) {
        log.debug("findUserCarByIdAndUserId({},{})", id, userId);
        return repository.findByIdAndUserId(id, userId);
    }

    public List<UserCar> findAllUserCarsByUserId(@NotNull UUID userId) {
        log.debug("findAllUserCarsByUserId({})", userId);
        return repository.findAllByUserId(userId);
    }
}
