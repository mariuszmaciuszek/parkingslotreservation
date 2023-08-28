package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlotJpaRepository extends JpaRepository<ParkingSlot, UUID> {
    Optional<ParkingSlot> findFirstByParkingIdAndAvailabilityAndSize(
            UUID parkingId, ParkingSlotAvailability availability, ParkingSlotSize size);

    Optional<ParkingSlot> findByIdAndParkingId(UUID id, UUID parkingId);

    List<ParkingSlot> findAllByParkingId(UUID parkingId);
}
