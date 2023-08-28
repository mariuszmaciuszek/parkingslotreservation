package com.mariuszmaciuszek.parkingslotreservation.reservations.user;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.dto.UserResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.Address;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.User;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseBody
    public UserResponse getUserById(@PathVariable UUID id) {
        log.info("getUserById({})", id);
        try {
            final User user = userService.getUserByUserId(id);
            return buildResponse(user);
        } catch (UserNotFoundException e) {
            log.error("getUserById() error occurred", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    @ResponseBody
    public List<UserResponse> listAllUsers() {
        log.info("listAllUsers");
        final List<User> users = userService.findAllUsers();
        return users.stream().map(this::buildResponse).toList();
    }

    private UserResponse buildResponse(User user) {
        final Address address = user.getAddress();
        final UserResponse.Address responseAddress = UserResponse.Address.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .streetLine1(address.getStreetLine1())
                .streetLine2(address.getStreetLine2())
                .postcode(address.getPostcode())
                .build();
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .id(user.getId())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .address(responseAddress)
                .build();
    }
}
