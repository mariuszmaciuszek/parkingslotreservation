package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class UserCarNotFoundException extends RuntimeException {
    public static final String USER_CAR_NOT_FOUND_MSG = "Cannot find car with id=%s";

    public UserCarNotFoundException(UUID userCarId) {
        super(String.format(USER_CAR_NOT_FOUND_MSG, userCarId));
    }
}
