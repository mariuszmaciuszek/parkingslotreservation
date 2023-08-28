package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Parking;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingJpaRepository extends JpaRepository<Parking, UUID> {}
