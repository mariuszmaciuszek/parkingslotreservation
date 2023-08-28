package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.*;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.ActivateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CancelReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CreateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.ReservationResponse;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/reservations")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    @NotNull
    private final ReservationService reservationService;

    @PostMapping
    @ResponseBody
    public ReservationResponse createReservation(@RequestBody @Valid CreateReservationRequest reservationRequest) {
        log.info("createReservation({})", reservationRequest);
        try {
            final Reservation reservation = reservationService.createReservation(reservationRequest);
            return buildResponse(reservation);
        } catch (UserNotFoundException
                | ParkingNotFoundException
                | ParkingSlotNotFoundException
                | ReservationNotFoundException
                | UserCarNotFoundException e) {
            log.error("createReservation error occurred ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ReservationPeriodMismatchException e) {
            log.error("createReservation error occurred ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{reservationId}/users/{userId}:cancel")
    @ResponseBody
    public ReservationResponse cancelReservation(@PathVariable UUID reservationId, @PathVariable UUID userId) {
        log.info("cancelReservation({},{})", reservationId, userId);
        final Reservation reservation =
                reservationService.cancelReservation(new CancelReservationRequest(reservationId, userId));
        try {
            return buildResponse(reservation);
        } catch (ReservationCannotBeCanceledException e) {
            log.error("cancelReservation error occurred ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UserNotFoundException | ReservationNotFoundException e) {
            log.error("cancelReservation error occurred ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/{reservationId}/users/{userId}:activate")
    @ResponseBody
    public ReservationResponse activateReservation(@PathVariable UUID reservationId, @PathVariable UUID userId) {
        log.info("activateReservation({},{})", reservationId, userId);
        final Reservation reservation =
                reservationService.activateReservation(new ActivateReservationRequest(reservationId, userId));
        try {
            return buildResponse(reservation);
        } catch (ReservationCannotBeActivatedException e) {
            log.error("activateReservation error occurred ", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UserNotFoundException | ReservationNotFoundException e) {
            log.error("activateReservation error occurred ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{reservationId}/users/{userId}")
    @ResponseBody
    public ReservationResponse getReservation(@PathVariable UUID reservationId, @PathVariable UUID userId) {
        log.info("getReservation({})", userId);
        final Reservation reservation = reservationService.getReservation(reservationId, userId);
        try {
            return buildResponse(reservation);
        } catch (ReservationNotFoundException e) {
            log.error("getReservation()", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/-/users/{userId}")
    @ResponseBody
    public List<ReservationResponse> listReservationsByUser(@PathVariable UUID userId) {
        log.info("listReservationsByUser({})", userId);
        final List<Reservation> reservations = reservationService.listAllReservationsByUserId(userId);
        return reservations.stream().map(this::buildResponse).toList();
    }

    private ReservationResponse buildResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getUserCarId(),
                reservation.getParkingSlotId(),
                reservation.getState(),
                reservation.getStartedDate(),
                reservation.getEndedDate(),
                reservation.getCreatedDate());
    }
}
