package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.abouttostartreminder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository.OutboxRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationAboutToStartManagerTest {

    @SpyBean
    private ReservationRepository reservationRepository;

    @SpyBean
    private OutboxRepository outboxRepository;

    @Autowired
    @InjectMocks
    private ReservationAboutToStartManager reservationAboutToStartManager;

    @Captor
    private ArgumentCaptor<List<OutboxEvent>> captor;

    private static final int SEARCHING_PERIOD_IN_MINUTES = 200;

    @BeforeEach
    void setUp() {
        reset(reservationRepository, outboxRepository);
    }

    @Test
    void process() {
        final List<Reservation> reservations =
                reservationRepository.findAllReservationAboutToStart(SEARCHING_PERIOD_IN_MINUTES);
        assertThat(reservations).isNotEmpty();

        reservationAboutToStartManager.process();

        verify(outboxRepository).saveAll(captor.capture());
        final List<OutboxEvent> outboxEventCreated = captor.getValue();
        assertThat(outboxEventCreated).hasSize(reservations.size()).allMatch(e -> e.getEventType()
                .equals(OutboxEvent.OutboxEventType.RESERVATION_START_REMINDER));
        assertThat(outboxEventCreated.stream().map(OutboxEvent::getReservationId))
                .containsExactlyInAnyOrderElementsOf(
                        reservations.stream().map(Reservation::getId).toList());
    }
}
