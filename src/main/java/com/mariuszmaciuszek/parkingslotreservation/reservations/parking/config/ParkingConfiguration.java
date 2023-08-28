package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.config;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository.ParkingJpaRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository.ParkingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParkingConfiguration {

    @Bean
    public ParkingRepository parkingRepository(ParkingJpaRepository parkingJpaRepository) {
        return new ParkingRepository(parkingJpaRepository);
    }
}
