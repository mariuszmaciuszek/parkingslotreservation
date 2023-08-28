package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.config;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository.ParkingSlotJpaRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository.ParkingSlotRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParkingSlotConfiguration {

    @Bean
    public ParkingSlotRepository parkingSlotRepository(ParkingSlotJpaRepository parkingSlotJpaRepository) {
        return new ParkingSlotRepository(parkingSlotJpaRepository);
    }
}
