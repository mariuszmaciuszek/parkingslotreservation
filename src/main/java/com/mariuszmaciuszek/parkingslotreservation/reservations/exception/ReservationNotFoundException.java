package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class ReservationNotFoundException extends RuntimeException {
    public static final String CANNOT_CANCEL_MSG = "Cannot find reservation with id=%s";

    public ReservationNotFoundException(UUID id) {
        super(String.format(CANNOT_CANCEL_MSG, id));
    }
}
