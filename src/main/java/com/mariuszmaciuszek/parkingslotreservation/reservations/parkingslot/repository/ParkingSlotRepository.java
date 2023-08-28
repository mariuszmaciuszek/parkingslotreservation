package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotAvailability;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotSize;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Slf4j
@Validated
public class ParkingSlotRepository {
    private final ParkingSlotJpaRepository repository;

    public Optional<ParkingSlot> findParkingSlotById(@NotNull UUID id) {
        log.debug("findParkingSlotById({})", id);
        return repository.findById(id);
    }

    public Optional<ParkingSlot> findParkingSlotById(@NotNull UUID id, @NotNull UUID parkingId) {
        log.debug("findParkingSlotById({},{})", id, parkingId);
        return repository.findByIdAndParkingId(id, parkingId);
    }

    public boolean parkingSlotExists(@NotNull UUID id) {
        log.debug("parkingSlotExists({})", id);
        return repository.existsById(id);
    }

    public Optional<ParkingSlot> findAvailableParkingSlot(
            @NotNull UUID parkingId, @NotNull ParkingSlotSize parkingSlotSize) {
        log.debug("findAvailableParkingSlot({},{})", parkingId, parkingSlotSize);
        return repository.findFirstByParkingIdAndAvailabilityAndSize(
                parkingId, ParkingSlotAvailability.AVAILABLE, parkingSlotSize);
    }

    public ParkingSlot save(@NotNull ParkingSlot parkingSlot) {
        log.debug("save({})", parkingSlot);
        return repository.save(parkingSlot);
    }

    public List<ParkingSlot> findAllByParkingId(@NotNull UUID parkingId) {
        log.debug("findAllByParkingId({})", parkingId);
        return repository.findAllByParkingId(parkingId);
    }
}
