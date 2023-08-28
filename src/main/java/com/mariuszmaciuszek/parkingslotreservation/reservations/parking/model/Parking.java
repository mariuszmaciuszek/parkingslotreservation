package com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parkings")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parking {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParkingType type;

    @Embedded
    private Address address;

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
}
