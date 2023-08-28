package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_cars")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCar {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String plate;

    @Column(name = "car_size", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarSize carSize;

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
