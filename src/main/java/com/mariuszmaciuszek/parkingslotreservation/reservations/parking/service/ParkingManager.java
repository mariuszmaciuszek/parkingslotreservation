package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ParkingNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Parking;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository.ParkingRepository;
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
public class ParkingManager implements ParkingService {

    private final ParkingRepository parkingRepository;

    @Override
    public Parking findParkingById(@NotNull UUID id) {
        log.info("findParkingById({})", id);
        return parkingRepository.findParkingById(id).orElseThrow(() -> new ParkingNotFoundException(id));
    }

    @Override
    public List<Parking> findAllParkings() {
        return parkingRepository.listParkings();
    }
}
