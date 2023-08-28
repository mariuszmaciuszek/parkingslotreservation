package com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ParkingSlotNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.dto.ParkingSlotResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotAvailability;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotSize;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.service.ParkingSlotService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/parkingSlots")
@RequiredArgsConstructor
@Slf4j
public class ParkingSlotController {
    private final ParkingSlotService parkingSlotService;

    @GetMapping("/{id}/parkings/{parkingId}")
    @ResponseBody
    public ParkingSlotResponse getParkingSlot(@PathVariable UUID id, @PathVariable UUID parkingId) {
        log.info("getParkingSlot({},{})", id, parkingId);
        try {
            final ParkingSlot parkingSlot = parkingSlotService.findParkingSlotByIdAndParkingId(id, parkingId);
            return buildResponse(parkingSlot);
        } catch (ParkingSlotNotFoundException e) {
            log.error("getParkingSlot() error occurred ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/-/parkings/{parkingId}")
    @ResponseBody
    public List<ParkingSlotResponse> listParkingSlots(@PathVariable UUID parkingId) {
        log.info("listParkingSlots({})", parkingId);
        final List<ParkingSlot> parkingSlots = parkingSlotService.findAllParkingSlotsByParkingId(parkingId);
        return parkingSlots.stream().map(this::buildResponse).toList();
    }

    private ParkingSlotResponse buildResponse(ParkingSlot parkingSlot) {
        return ParkingSlotResponse.builder()
                .parkingId(parkingSlot.getParkingId())
                .id(parkingSlot.getId())
                .size(parkingSlotSize(parkingSlot.getSize()))
                .availability(parkingSpotAvailability(parkingSlot.getAvailability()))
                .createdDate(parkingSlot.getCreatedDate())
                .updatedDate(parkingSlot.getUpdatedDate())
                .build();
    }

    private ParkingSlotResponse.ParkingSlotAvailability parkingSpotAvailability(ParkingSlotAvailability availability) {
        if (Objects.isNull(availability)) {
            return null;
        }
        return ParkingSlotResponse.ParkingSlotAvailability.valueOf(availability.toString());
    }

    private ParkingSlotResponse.ParkingSlotSize parkingSlotSize(ParkingSlotSize size) {
        if (Objects.isNull(size)) {
            return null;
        }
        return ParkingSlotResponse.ParkingSlotSize.valueOf(size.toString());
    }
}
