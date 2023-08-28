package com.mariuszmaciuszek.parkingslotreservation.reservations.parking;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.dto.ParkingResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Address;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.Parking;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.model.ParkingType;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.service.ParkingService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/parkings")
@RequiredArgsConstructor
@Slf4j
public class ParkingController {
    private final ParkingService parkingService;

    @GetMapping("/{id}")
    @ResponseBody
    public ParkingResponse getParkingById(@PathVariable UUID id) {
        log.info("getParkingById({})", id);
        try {
            final Parking parking = parkingService.findParkingById(id);
            return buildResponse(parking);
        } catch (UserNotFoundException e) {
            log.error("getUserById() error occurred", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    @ResponseBody
    public List<ParkingResponse> listAllParkings() {
        log.info("listAllParkings");
        final List<Parking> parkings = parkingService.findAllParkings();
        return parkings.stream().map(this::buildResponse).toList();
    }

    private ParkingResponse buildResponse(Parking parking) {
        final Address address = parking.getAddress();
        final ParkingResponse.Address responseAddress = ParkingResponse.Address.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .streetLine1(address.getStreetLine1())
                .streetLine2(address.getStreetLine2())
                .postcode(address.getPostcode())
                .build();
        return ParkingResponse.builder()
                .id(parking.getId())
                .name(parking.getName())
                .type(parkingType(parking.getType()))
                .createdDate(parking.getCreatedDate())
                .updatedDate(parking.getUpdatedDate())
                .address(responseAddress)
                .build();
    }

    private ParkingResponse.ParkingType parkingType(ParkingType type) {
        if (Objects.isNull(type)) {
            return null;
        }
        return ParkingResponse.ParkingType.valueOf(type.toString());
    }
}
