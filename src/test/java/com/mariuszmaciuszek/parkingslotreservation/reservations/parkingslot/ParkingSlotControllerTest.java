package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot;

import static com.mariuszmaciuszek.parkingslotreservation.fixtures.ParkingFixtures.PARKING_OUTDOOR_ID;
import static com.mariuszmaciuszek.parkingslotreservation.fixtures.ParkingSlotFixtures.PARKING_SLOT_LARGE_AVAILABLE_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.dto.ParkingSlotResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository.ParkingSlotRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParkingSlotControllerTest {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void getParkingSlot() {
        final Optional<ParkingSlot> result = parkingSlotRepository.findParkingSlotById(PARKING_SLOT_LARGE_AVAILABLE_ID);
        assertThat(result).isPresent();
        final ParkingSlot dbParkingSlot = result.get();
        final String url = urlFor(
                "/api/v1alpha1/parkingSlots/" + PARKING_SLOT_LARGE_AVAILABLE_ID + "/parkings/" + PARKING_OUTDOOR_ID);
        final ResponseEntity<ParkingSlotResponse> response = restTemplate.getForEntity(url, ParkingSlotResponse.class);
        final ParkingSlotResponse parkingSlotResponse = response.getBody();
        assertThat(parkingSlotResponse).isNotNull();
        assertThat(parkingSlotResponse.id()).isEqualTo(PARKING_SLOT_LARGE_AVAILABLE_ID);
        assertThat(parkingSlotResponse.parkingId()).isEqualTo(PARKING_OUTDOOR_ID);
        assertThat(parkingSlotResponse.availability().toString())
                .hasToString(dbParkingSlot.getAvailability().toString());
        assertThat(parkingSlotResponse.size().toString())
                .hasToString(dbParkingSlot.getSize().toString());
        assertThat(parkingSlotResponse.createdDate()).isEqualTo(dbParkingSlot.getCreatedDate());
        assertThat(parkingSlotResponse.updatedDate()).isEqualTo(dbParkingSlot.getUpdatedDate());
    }

    @Test
    void listParkingSlots() {
        final int carCount =
                parkingSlotRepository.findAllByParkingId(PARKING_OUTDOOR_ID).size();
        final String url = urlFor("/api/v1alpha1/parkingSlots/-/parkings/" + PARKING_OUTDOOR_ID);

        final ResponseEntity<ParkingSlotResponse[]> response =
                restTemplate.getForEntity(url, ParkingSlotResponse[].class);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(carCount);
    }

    private String urlFor(String path) {
        return "http://localhost:" + port + path;
    }
}
