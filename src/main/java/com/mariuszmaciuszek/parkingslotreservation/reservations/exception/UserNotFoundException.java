package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public static final String USER_NOT_FOUND_MSG = "Cannot find user with id=%s";

    public UserNotFoundException(UUID userId) {
        super(String.format(USER_NOT_FOUND_MSG, userId));
    }
}
