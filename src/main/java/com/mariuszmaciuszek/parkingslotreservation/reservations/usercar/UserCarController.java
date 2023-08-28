package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.dto.UserCarResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.CarSize;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.service.UserCarService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/userCars")
@RequiredArgsConstructor
@Slf4j
public class UserCarController {

    private final UserCarService userCarService;

    @GetMapping("/{id}/users/{userId}")
    @ResponseBody
    public UserCarResponse getUserCar(@PathVariable UUID id, @PathVariable UUID userId) {
        log.info("getUserCar({},{})", id, userId);
        try {
            final UserCar userCar = userCarService.findUserCar(id, userId);
            return buildResponse(userCar);
        } catch (UserNotFoundException e) {
            log.error("getUserById() error occurred", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/-/users/{userId}")
    @ResponseBody
    public List<UserCarResponse> listAllUserCarsForUser(@PathVariable UUID userId) {
        log.info("listAllUserCarsForUser({})", userId);
        final List<UserCar> userCars = userCarService.findUserCars(userId);
        return userCars.stream().map(this::buildResponse).toList();
    }

    private UserCarResponse buildResponse(UserCar userCar) {
        return UserCarResponse.builder()
                .createdDate(userCar.getCreatedDate())
                .updatedDate(userCar.getUpdatedDate())
                .plate(userCar.getPlate())
                .userId(userCar.getUserId())
                .id(userCar.getId())
                .carSize(userCarSize(userCar.getCarSize()))
                .build();
    }

    private UserCarResponse.CarSize userCarSize(CarSize carSize) {
        if (Objects.isNull(carSize)) {
            return null;
        }
        return UserCarResponse.CarSize.valueOf(carSize.toString());
    }
}
