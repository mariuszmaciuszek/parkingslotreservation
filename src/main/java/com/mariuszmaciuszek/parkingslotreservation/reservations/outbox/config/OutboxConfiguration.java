package com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.config;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository.OutboxJpaRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository.OutboxRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxConfiguration {
    @Bean
    public OutboxRepository outboxRepository(OutboxJpaRepository outboxJpaRepository) {
        return new OutboxRepository(outboxJpaRepository);
    }
}
