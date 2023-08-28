package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface UserCarService {

    UserCar findUserCar(@NotNull UUID id, @NotNull UUID userId);

    List<UserCar> findUserCars(@NotNull UUID userId);
}
