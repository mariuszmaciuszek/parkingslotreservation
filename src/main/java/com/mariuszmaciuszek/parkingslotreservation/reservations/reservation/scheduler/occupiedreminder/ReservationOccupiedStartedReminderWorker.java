package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.occupiedreminder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "app.scheduler.enabled")
@RequiredArgsConstructor
public class ReservationOccupiedStartedReminderWorker {
    private final ReservationOccupiedStartedManager reservationOccupiedStartedManager;

    @Scheduled(fixedDelayString = "${app.scheduler.fixedDelay}", initialDelayString = "${app.scheduler.initialDelay}")
    public void run() {
        try {
            reservationOccupiedStartedManager.process();
        } catch (RuntimeException e) {
            log.error("Error ", e);
        }
    }
}
