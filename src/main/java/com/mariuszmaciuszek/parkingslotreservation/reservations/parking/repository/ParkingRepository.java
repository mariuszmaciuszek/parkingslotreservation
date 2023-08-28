package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Parking;
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
public class ParkingRepository {
    private final ParkingJpaRepository repository;

    public Optional<Parking> findParkingById(@NotNull UUID id) {
        log.debug("findParkingById({})", id);
        return repository.findById(id);
    }

    public boolean parkingExists(@NotNull UUID id) {
        log.debug("parkingExists({})", id);
        return repository.existsById(id);
    }

    public List<Parking> listParkings() {
        return repository.findAll();
    }
}
