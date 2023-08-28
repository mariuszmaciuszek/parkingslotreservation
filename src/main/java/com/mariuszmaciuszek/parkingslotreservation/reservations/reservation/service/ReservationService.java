package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.service;

import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.ActivateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CancelReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.dto.CreateReservationRequest;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface ReservationService {

    Reservation createReservation(@NotNull @Valid CreateReservationRequest reservationRequest);

    Reservation cancelReservation(@NotNull @Valid CancelReservationRequest cancelRequest);

    Reservation activateReservation(@NotNull @Valid ActivateReservationRequest activateReservationRequest);

    Reservation getReservation(@NotNull UUID id, @NotNull UUID userId);

    List<Reservation> listAllReservationsByUserId(@NotNull UUID userId);
}
