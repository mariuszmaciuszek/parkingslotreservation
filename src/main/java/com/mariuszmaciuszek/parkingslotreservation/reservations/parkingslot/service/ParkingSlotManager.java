package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ParkingSlotNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository.ParkingSlotRepository;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class ParkingSlotManager implements ParkingSlotService {
    private final ParkingSlotRepository parkingSlotRepository;

    @Override
    public ParkingSlot findParkingSlotByIdAndParkingId(@NotNull UUID id, @NotNull UUID parkingId) {
        log.info("findParkingSlotByIdAndParkingId({},{})", id, parkingId);
        return parkingSlotRepository
                .findParkingSlotById(id, parkingId)
                .orElseThrow(() -> new ParkingSlotNotFoundException(id));
    }

    @Override
    public List<ParkingSlot> findAllParkingSlotsByParkingId(@NotNull UUID parkingId) {
        log.info("findAllParkingSlotsByParkingId({})", parkingId);
        return parkingSlotRepository.findAllByParkingId(parkingId);
    }
}
