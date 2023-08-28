package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation;

import static com.mariuszmaciuszek.parkingslotreservation.fixtures.ReservationFixtures.RESERVATION_RESERVED_JOHN_LARGE_CAR;
import static com.mariuszmaciuszek.parkingslotreservation.fixtures.UserFixtures.JOHN_USER_ID;
import static com.mariuszmaciuszek.parkingslotreservation.fixtures.UserFixtures.MARK_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.mariuszmaciuszek.parkingslotreservation.fixtures.ParkingFixtures;
import com.mariuszmaciuszek.parkingslotreservation.fixtures.UserCarFixtures;
import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CreateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.ReservationResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReservationJpaRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        repository
                .findReservationByIdAndUserId(RESERVATION_RESERVED_JOHN_LARGE_CAR, JOHN_USER_ID)
                .ifPresent(r -> {
                    r.setState(ReservationState.RESERVED);
                    repository.save(r);
                });
    }

    @Test
    void createReservation() {
        final String url = urlFor("/api/v1alpha1/reservations");
        final CreateReservationRequest request =
                buildCreateReservationRequestBuilder().build();
        final ResponseEntity<ReservationResponse> response =
                restTemplate.postForEntity(url, request, ReservationResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        final ReservationResponse reservation = response.getBody();
        assertThat(reservation).isNotNull();
        assertThat(reservation.userId()).isEqualTo(request.userId());
        assertThat(reservation.state()).isEqualTo(ReservationState.RESERVED);
        assertThat(reservation.userCarId()).isEqualTo(request.userCarId());
        assertThat(reservation.startedDate()).isEqualTo(request.startTime());
        assertThat(reservation.endedDate()).isEqualTo(request.endTime());
        assertThat(reservation.parkingSlotId()).isNotNull();

        repository.deleteById(reservation.id());
    }

    @Test
    void cancelReservation() {
        final String url = urlFor("/api/v1alpha1/reservations/" + RESERVATION_RESERVED_JOHN_LARGE_CAR + "/users/"
                + JOHN_USER_ID + ":cancel");

        final ReservationResponse reservation = restTemplate.patchForObject(url, null, ReservationResponse.class);
        assertThat(reservation).isNotNull();
        assertThat(reservation.userId()).isEqualTo(JOHN_USER_ID);
        assertThat(reservation.state()).isEqualTo(ReservationState.CANCELED);
        assertThat(reservation.id()).isEqualTo(RESERVATION_RESERVED_JOHN_LARGE_CAR);
    }

    @Test
    void activateReservation() {
        final String url = urlFor("/api/v1alpha1/reservations/" + RESERVATION_RESERVED_JOHN_LARGE_CAR + "/users/"
                + JOHN_USER_ID + ":activate");

        final ReservationResponse reservation = restTemplate.patchForObject(url, null, ReservationResponse.class);
        assertThat(reservation).isNotNull();
        assertThat(reservation.userId()).isEqualTo(JOHN_USER_ID);
        assertThat(reservation.state()).isEqualTo(ReservationState.IN_USE);
        assertThat(reservation.id()).isEqualTo(RESERVATION_RESERVED_JOHN_LARGE_CAR);
    }

    @Test
    void getReservation() {
        final String url =
                urlFor("/api/v1alpha1/reservations/" + RESERVATION_RESERVED_JOHN_LARGE_CAR + "/users/" + JOHN_USER_ID);

        final ResponseEntity<ReservationResponse> response = restTemplate.getForEntity(url, ReservationResponse.class);
        final ReservationResponse reservation = response.getBody();
        assertThat(reservation).isNotNull();
        assertThat(reservation.userId()).isEqualTo(JOHN_USER_ID);
        assertThat(reservation.state()).isEqualTo(ReservationState.RESERVED);
        assertThat(reservation.id()).isEqualTo(RESERVATION_RESERVED_JOHN_LARGE_CAR);
    }

    @Test
    void listReservationsByUser() {
        final String url = urlFor("/api/v1alpha1/reservations/-/users/" + JOHN_USER_ID);

        final ResponseEntity<ReservationResponse[]> response =
                restTemplate.getForEntity(url, ReservationResponse[].class);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    private String urlFor(String path) {
        return "http://localhost:" + port + path;
    }

    public static CreateReservationRequest.CreateReservationRequestBuilder buildCreateReservationRequestBuilder() {
        return CreateReservationRequest.builder()
                .userId(MARK_USER_ID)
                .userCarId(UserCarFixtures.USER_CAR_ID_MEDIUM_FOR_MARK)
                .parkingId(ParkingFixtures.PARKING_OUTDOOR_ID)
                .endTime(DateTimeUtils.now().plusHours(2))
                .startTime(DateTimeUtils.now().plusHours(1));
    }
}
