package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationCannotBeActivatedException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationCannotBeCanceledException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationCreationException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command.ActivateReservationCommand;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command.CancelReservationCommand;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command.CreateReservationCommand;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.ActivateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CancelReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CreateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class ReservationManager implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CreateReservationCommand createReservationCommand;
    private final CancelReservationCommand cancelReservationCommand;
    private final ActivateReservationCommand activateReservationCommand;

    @Override
    public Reservation createReservation(@NotNull @Valid CreateReservationRequest reservationRequest) {
        log.info("createReservation({})", reservationRequest);
        return createReservationCommand
                .run(buildFromRequest(reservationRequest))
                .orElseThrow(ReservationCreationException.ReservationCreationInternalException::new);
    }

    @Override
    public Reservation cancelReservation(@NotNull @Valid CancelReservationRequest cancelRequest) {
        log.info("cancelReservation({})", cancelRequest);
        final CancelReservationCommand.CancelReservationCommandDescriptor cancelReservationCommandDescriptor =
                CancelReservationCommand.CancelReservationCommandDescriptor.builder()
                        .reservationId(cancelRequest.reservationId())
                        .userId(cancelRequest.userId())
                        .build();
        return cancelReservationCommand
                .run(cancelReservationCommandDescriptor)
                .orElseThrow(() -> new ReservationCannotBeCanceledException(cancelRequest.reservationId()));
    }

    @Override
    public Reservation activateReservation(@NotNull @Valid ActivateReservationRequest activateReservationRequest) {
        log.info("activateReservation({})", activateReservationRequest);
        final ActivateReservationCommand.ActivateCommandDescriptor activateCommandDescriptor =
                ActivateReservationCommand.ActivateCommandDescriptor.builder()
                        .reservationId(activateReservationRequest.reservationId())
                        .userId(activateReservationRequest.userId())
                        .build();
        return activateReservationCommand
                .run(activateCommandDescriptor)
                .orElseThrow(
                        () -> new ReservationCannotBeActivatedException(activateReservationRequest.reservationId()));
    }

    @Override
    public Reservation getReservation(@NotNull UUID id, @NotNull UUID userId) {
        log.info("getReservation({},{})", id, userId);
        return reservationRepository
                .findReservationByIdAndUserId(id, userId)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Override
    public List<Reservation> listAllReservationsByUserId(@NotNull UUID userId) {
        log.info("listAllReservationsByUserId({})", userId);
        return reservationRepository.findAllReservationsByUserId(userId);
    }

    private CreateReservationCommand.CreateReservationCommandDescriptor buildFromRequest(
            CreateReservationRequest reservationRequest) {
        return CreateReservationCommand.CreateReservationCommandDescriptor.builder()
                .userId(reservationRequest.userId())
                .userCarId(reservationRequest.userCarId())
                .parkingId(reservationRequest.parkingId())
                .startDate(reservationRequest.startTime())
                .endDate(reservationRequest.endTime())
                .build();
    }
}
