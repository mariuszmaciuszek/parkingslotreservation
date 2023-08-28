package com.mariuszmaciuszek.parkingslotreservation.reservations.user.config;

import com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository.UserJpaRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository) {
        return new UserRepository(userJpaRepository);
    }
}
