package com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.model.OutboxEvent;
import com.mariuszmaciuszek.parkingslotreservation.reservations.outbox.repository.OutboxRepository;
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
class OutboxManagerTest {
    @SpyBean
    private OutboxRepository repository;

    @Captor
    private ArgumentCaptor<List<OutboxEvent>> captor;

    private OutboxManager outboxManager;

    @BeforeEach
    void setUp() {
        reset(repository);
        outboxManager = new OutboxManager(repository);
    }

    @Test
    void processEvents() {
        final List<OutboxEvent> events = repository.findNotProcessedEvents();
        assertThat(events).isNotEmpty();

        outboxManager.processEvents();
        verify(repository).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(events.size());
    }
}
