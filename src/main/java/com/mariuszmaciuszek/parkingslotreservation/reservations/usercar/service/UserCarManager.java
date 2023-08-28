package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserCarNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository.UserCarRepository;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserCarManager implements UserCarService {

    private final UserCarRepository userCarRepository;

    @Override
    public UserCar findUserCar(@NotNull UUID id, @NotNull UUID userId) {
        log.info("findUserCar({},{})", id, userId);
        return userCarRepository
                .findUserCarByIdAndUserId(id, userId)
                .orElseThrow(() -> new UserCarNotFoundException(id));
    }

    @Override
    public List<UserCar> findUserCars(@NotNull UUID userId) {
        log.info("findUserCars({})", userId);
        return userCarRepository.findAllUserCarsByUserId(userId);
    }
}
