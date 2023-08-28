package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.abouttostartreminder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "app.scheduler.enabled")
@RequiredArgsConstructor
public class ReservationAboutToStartReminderWorker {
    private final ReservationAboutToStartManager reservationAboutToStartManager;

    @Scheduled(fixedDelayString = "${app.scheduler.fixedDelay}", initialDelayString = "${app.scheduler.initialDelay}")
    public void run() {
        try {
            reservationAboutToStartManager.process();
        } catch (RuntimeException e) {
            log.error("Error ", e);
        }
    }
}
