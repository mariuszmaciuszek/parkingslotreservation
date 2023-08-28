package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class ReservationCreationException extends RuntimeException {
    public static final String RESERVATION_NOT_CREATED_MSG =
            "Reservation was not create probably internal error has occurred";
    public static final String CAR_HAS_ACTIVE_RESERVATION_MSG = "Car with userCarId=%s has active reservation";

    private ReservationCreationException(String msg) {
        super(msg);
    }

    public static class CarHasActiveReservationException extends ReservationCreationException {
        public CarHasActiveReservationException(UUID userCarId) {
            super(String.format(CAR_HAS_ACTIVE_RESERVATION_MSG, userCarId));
        }
    }

    public static class ReservationCreationInternalException extends ReservationCreationException {
        public ReservationCreationInternalException() {
            super(RESERVATION_NOT_CREATED_MSG);
        }
    }
}
