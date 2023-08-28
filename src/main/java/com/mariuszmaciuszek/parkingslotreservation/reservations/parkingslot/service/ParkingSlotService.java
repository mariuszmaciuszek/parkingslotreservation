package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface ParkingSlotService {

    ParkingSlot findParkingSlotByIdAndParkingId(@NotNull UUID id, @NotNull UUID parkingId);

    List<ParkingSlot> findAllParkingSlotsByParkingId(@NotNull UUID parkingId);
}
