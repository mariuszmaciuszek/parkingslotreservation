package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.scheduler.occupiedreminder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationOccupiedStartedManagerTest {
    @SpyBean
    private ReservationRepository reservationRepository;

    @SpyBean
    private OutboxRepository outboxRepository;

    @Captor
    private ArgumentCaptor<List<Reservation>> captorReservation;

    @Captor
    private ArgumentCaptor<List<OutboxEvent>> captorEvent;

    private ReservationOccupiedStartedManager reservationOccupiedStartedManager;

    @BeforeEach
    void setUp() {
        reservationOccupiedStartedManager =
                new ReservationOccupiedStartedManager(reservationRepository, outboxRepository);
    }

    @Test
    void process() {
        int afterEndedTimeCount =
                reservationRepository.findAllReservationsInUseAfterEndedDate().size();
        int afterStartedTimeCount = reservationRepository
                .findAllReservationsReservedAfterStartDate()
                .size();

        reservationOccupiedStartedManager.process();
        doReturn(List.of()).when(reservationRepository).saveAll(any());
        doReturn(List.of()).when(outboxRepository).saveAll(any());

        verify(reservationRepository).saveAll(captorReservation.capture());
        assertThat(captorReservation.getValue()).hasSize(afterEndedTimeCount + afterStartedTimeCount);

        verify(outboxRepository).saveAll(captorEvent.capture());
        assertThat(captorEvent.getValue()).hasSize(afterEndedTimeCount + afterStartedTimeCount);
    }
}
