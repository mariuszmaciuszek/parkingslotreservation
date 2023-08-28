package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Parking;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface ParkingService {
    Parking findParkingById(@NotNull UUID id);

    List<Parking> findAllParkings();
}
