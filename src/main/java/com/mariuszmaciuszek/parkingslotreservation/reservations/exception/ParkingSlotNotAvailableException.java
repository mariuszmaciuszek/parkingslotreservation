package com.mariuszmaciuszek.parkingslotreservation.reservations.exception;

import java.util.UUID;

public class ParkingSlotNotAvailableException extends RuntimeException {
    public static final String PARKING_SLOT_NOT_AVAILABLE_MSG =
            "Parking at id=%s does not currently have any available parking space for your car";

    public ParkingSlotNotAvailableException(UUID parkingId) {
        super(String.format(PARKING_SLOT_NOT_AVAILABLE_MSG, parkingId));
    }
}
