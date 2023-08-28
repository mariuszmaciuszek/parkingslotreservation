package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class ReservationCannotBeCanceledException extends RuntimeException {
    public static final String CANNOT_CANCEL_MSG = "Cannot cancelReservation reservation with id=%s";

    public ReservationCannotBeCanceledException(UUID reservationId) {
        super(String.format(CANNOT_CANCEL_MSG, reservationId));
    }
}
