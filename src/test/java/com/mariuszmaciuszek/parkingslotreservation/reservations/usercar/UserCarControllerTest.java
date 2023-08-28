package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar;

import static com.mariuszmaciuszek.parkingslotreservation.fixtures.UserCarFixtures.USER_CAR_ID_MEDIUM_FOR_MARK;
import static com.mariuszmaciuszek.parkingslotreservation.fixtures.UserFixtures.MARK_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.dto.UserCarResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository.UserCarsJpaRepository;
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
class UserCarControllerTest {
    @Autowired
    private UserCarsJpaRepository repository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void getUserCar() {
        final Optional<UserCar> result = repository.findById(USER_CAR_ID_MEDIUM_FOR_MARK);
        assertThat(result).isPresent();
        final UserCar dbUserCar = result.get();

        final String url = urlFor("/api/v1alpha1/userCars/" + USER_CAR_ID_MEDIUM_FOR_MARK + "/users/" + MARK_USER_ID);

        final ResponseEntity<UserCarResponse> response = restTemplate.getForEntity(url, UserCarResponse.class);
        final UserCarResponse userCarResponse = response.getBody();
        assertThat(userCarResponse).isNotNull();
        assertThat(userCarResponse.id()).isEqualTo(dbUserCar.getId());
        assertThat(userCarResponse.carSize().toString())
                .hasToString(dbUserCar.getCarSize().toString());
        assertThat(userCarResponse.userId()).isEqualTo(dbUserCar.getUserId());
        assertThat(userCarResponse.plate()).isEqualTo(dbUserCar.getPlate());
        assertThat(userCarResponse.createdDate()).isEqualTo(dbUserCar.getCreatedDate());
        assertThat(userCarResponse.updatedDate()).isEqualTo(dbUserCar.getUpdatedDate());
    }

    @Test
    void listAllUserCarsForUser() {
        final int carCount = repository.findAllByUserId(MARK_USER_ID).size();
        final String url = urlFor("/api/v1alpha1/userCars/-/users/" + MARK_USER_ID);

        final ResponseEntity<UserCarResponse[]> response = restTemplate.getForEntity(url, UserCarResponse[].class);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(carCount);
    }

    private String urlFor(String path) {
        return "http://localhost:" + port + path;
    }
}
