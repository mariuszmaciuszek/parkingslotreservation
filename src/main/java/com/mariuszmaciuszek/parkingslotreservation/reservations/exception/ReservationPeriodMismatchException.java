package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

public class ReservationPeriodMismatchException extends RuntimeException {
    public static final String MSG = "The booking end time cannot be earlier than the booking start time";

    public ReservationPeriodMismatchException() {
        super(MSG);
    }
}
