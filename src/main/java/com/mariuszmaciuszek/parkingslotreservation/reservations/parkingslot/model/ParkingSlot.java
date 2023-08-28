package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_slots")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSlot {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID parkingId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParkingSlotSize size;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParkingSlotAvailability availability;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        final LocalDateTime now = DateTimeUtils.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = DateTimeUtils.now();
    }

    public void setUnavailable() {
        this.availability = ParkingSlotAvailability.NOT_AVAILABLE;
    }

    public void setAvailable() {
        this.availability = ParkingSlotAvailability.AVAILABLE;
    }
}
