package com.mariuszmaciuszek.parkingslotreservation.reservations.user;

import static com.mariuszmaciuszek.parkingslotreservation.fixtures.UserFixtures.SMITH_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.mariuszmaciuszek.parkingslotreservation.reservations.user.dto.UserResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.Address;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.User;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository.UserJpaRepository;
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
class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserJpaRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void getUserById() {
        final Optional<User> result = repository.findById(SMITH_USER_ID);
        assertThat(result).isPresent();
        final User dbUser = result.get();
        final Address dbAddress = dbUser.getAddress();
        assertThat(dbAddress).isNotNull();

        final String url = urlFor("/api/v1alpha1/users/" + SMITH_USER_ID);

        final ResponseEntity<UserResponse> response = restTemplate.getForEntity(url, UserResponse.class);
        final UserResponse userResponse = response.getBody();
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.id()).isEqualTo(SMITH_USER_ID);
        assertThat(userResponse.firstName()).isEqualTo(dbUser.getFirstName());
        assertThat(userResponse.lastName()).isEqualTo(dbUser.getLastName());
        assertThat(userResponse.birthDate()).isEqualTo(dbUser.getBirthDate());
        assertThat(userResponse.email()).isEqualTo(dbUser.getEmail());
        assertThat(userResponse.createdDate()).isEqualTo(dbUser.getCreatedDate());
        assertThat(userResponse.updatedDate()).isEqualTo(dbUser.getUpdatedDate());

        final UserResponse.Address responseAddress = userResponse.address();
        assertThat(responseAddress).isNotNull();
        assertThat(responseAddress.city()).isEqualTo(dbAddress.getCity());
        assertThat(responseAddress.postcode()).isEqualTo(dbAddress.getPostcode());
        assertThat(responseAddress.country()).isEqualTo(dbAddress.getCountry());
        assertThat(responseAddress.streetLine1()).isEqualTo(dbAddress.getStreetLine1());
        assertThat(responseAddress.streetLine2()).isEqualTo(dbAddress.getStreetLine2());
    }

    @Test
    void listAllUsers() {
        final String url = urlFor("/api/v1alpha1/users");

        final ResponseEntity<UserResponse[]> response = restTemplate.getForEntity(url, UserResponse[].class);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
    }

    private String urlFor(String path) {
        return "http://localhost:" + port + path;
    }
}
