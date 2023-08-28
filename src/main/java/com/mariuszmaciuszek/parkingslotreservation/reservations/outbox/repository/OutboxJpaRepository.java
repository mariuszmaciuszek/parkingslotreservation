package com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxJpaRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findAllByProcessed(OutboxEvent.OutboxEventProcessed processed);
}
