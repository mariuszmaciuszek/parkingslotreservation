package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class ParkingNotFoundException extends RuntimeException {
    public static final String PARKING_NOT_FOUND_MSG = "Cannot find parking with id=%s";

    public ParkingNotFoundException(UUID parkingSlotId) {
        super(String.format(PARKING_NOT_FOUND_MSG, parkingSlotId));
    }
}
