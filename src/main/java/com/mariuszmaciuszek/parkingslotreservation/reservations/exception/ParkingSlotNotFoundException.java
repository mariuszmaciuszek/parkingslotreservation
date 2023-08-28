package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class ParkingSlotNotFoundException extends RuntimeException {
    public static final String PARKING_SLOT_NOT_FOUND_MSG = "Cannot find parking slot with id=%s";

    public ParkingSlotNotFoundException(UUID parkingSlotId) {
        super(String.format(PARKING_SLOT_NOT_FOUND_MSG, parkingSlotId));
    }
}
