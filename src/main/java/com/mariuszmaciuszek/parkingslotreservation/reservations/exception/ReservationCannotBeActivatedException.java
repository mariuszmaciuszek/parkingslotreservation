package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class ReservationCannotBeActivatedException extends RuntimeException {
    public static final String CANNOT_ACTIVATE_MSG = "Cannot activate  reservation with id=%s";

    public ReservationCannotBeActivatedException(UUID reservationId) {
        super(String.format(CANNOT_ACTIVATE_MSG, reservationId));
    }
}
