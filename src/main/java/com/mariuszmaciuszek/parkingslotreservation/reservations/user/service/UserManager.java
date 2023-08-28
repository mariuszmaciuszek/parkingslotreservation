package com.mariuszmaciuszek.parkingslotreservation.reservations.user.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.User;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserManager implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserByUserId(@NotNull UUID id) {
        log.info("getUserByUserId({})", id);
        return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }
}
