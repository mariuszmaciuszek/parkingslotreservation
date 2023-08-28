package com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboxEvent {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID reservationId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxEventType eventType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutboxEventProcessed processed;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    public void markProcessed() {
        processed = OutboxEventProcessed.TRUE;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = DateTimeUtils.now();
        processed = OutboxEventProcessed.FALSE;
    }

    public enum OutboxEventType {
        RESERVATION_START_REMINDER,
        RESERVATION_OCCUPIED,
        RESERVATION_END_REMINDER
    }

    public enum OutboxEventProcessed {
        TRUE,
        FALSE
    }
}
