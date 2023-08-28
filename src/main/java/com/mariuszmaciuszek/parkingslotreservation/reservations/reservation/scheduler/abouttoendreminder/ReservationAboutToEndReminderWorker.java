package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.abouttoendreminder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.scheduler.enabled")
public class ReservationAboutToEndReminderWorker {
    private final ReservationAboutToEndManager reservationAboutToEndManager;

    @Scheduled(fixedDelayString = "${app.scheduler.fixedDelay}", initialDelayString = "${app.scheduler.initialDelay}")
    public void run() {
        try {
            reservationAboutToEndManager.process();
        } catch (RuntimeException e) {
            log.error("Error ", e);
        }
    }
}
