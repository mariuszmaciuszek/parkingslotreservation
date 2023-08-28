package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.config;

import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationJpaRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationConfiguration {
    @Bean
    public ReservationRepository reservationRepository(ReservationJpaRepository reservationJpaRepository) {
        return new ReservationRepository(reservationJpaRepository);
    }
}
