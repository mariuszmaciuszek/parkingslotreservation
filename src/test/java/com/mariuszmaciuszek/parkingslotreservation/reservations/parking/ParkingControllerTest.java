package com.mariuszmaciuszek.parkingslotreservation.reservations.parking;

import static com.mariuszmaciuszek.parkingslotreservation.fixtures.ParkingFixtures.PARKING_INDOOR_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.dto.ParkingResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Address;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Parking;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository.ParkingJpaRepository;
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
class ParkingControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ParkingJpaRepository parkingJpaRepository;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void getParkingById() {
        final Optional<Parking> result = parkingJpaRepository.findById(PARKING_INDOOR_ID);
        assertThat(result).isPresent();
        final Parking dbParking = result.get();
        final Address dbAddress = dbParking.getAddress();
        assertThat(dbAddress).isNotNull();

        final String url = urlFor("/api/v1alpha1/parkings/" + PARKING_INDOOR_ID);
        final ResponseEntity<ParkingResponse> response = restTemplate.getForEntity(url, ParkingResponse.class);
        final ParkingResponse parkingResponse = response.getBody();
        assertThat(parkingResponse).isNotNull();
        assertThat(parkingResponse.id()).isEqualTo(PARKING_INDOOR_ID);
        assertThat(parkingResponse.type().toString())
                .hasToString(dbParking.getType().toString());
        assertThat(parkingResponse.createdDate()).isEqualTo(dbParking.getCreatedDate());
        assertThat(parkingResponse.updatedDate()).isEqualTo(dbParking.getUpdatedDate());

        final ParkingResponse.Address responseAddress = parkingResponse.address();
        assertThat(responseAddress).isNotNull();
        assertThat(responseAddress.city()).isEqualTo(dbAddress.getCity());
        assertThat(responseAddress.postcode()).isEqualTo(dbAddress.getPostcode());
        assertThat(responseAddress.country()).isEqualTo(dbAddress.getCountry());
        assertThat(responseAddress.streetLine1()).isEqualTo(dbAddress.getStreetLine1());
        assertThat(responseAddress.streetLine2()).isEqualTo(dbAddress.getStreetLine2());
    }

    @Test
    void listAllParkings() {
        final int parkingCount = parkingJpaRepository.findAll().size();
        final String url = urlFor("/api/v1alpha1/parkings");
        final ResponseEntity<ParkingResponse[]> response = restTemplate.getForEntity(url, ParkingResponse[].class);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(parkingCount);
    }

    private String urlFor(String path) {
        return "http://localhost:" + port + path;
    }
}
