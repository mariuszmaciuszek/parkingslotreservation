package com.mariuszmaciuszek.parkingslotreservation.reservations.user.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserByUserId(@NotNull UUID id);

    List<User> findAllUsers();
}
