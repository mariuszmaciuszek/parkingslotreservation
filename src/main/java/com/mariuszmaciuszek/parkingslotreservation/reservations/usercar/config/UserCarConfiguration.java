package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.config;

import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository.UserCarRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository.UserCarsJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCarConfiguration {

    @Bean
    public UserCarRepository userCarRepository(UserCarsJpaRepository userCarsJpaRepository) {
        return new UserCarRepository(userCarsJpaRepository);
    }
}
