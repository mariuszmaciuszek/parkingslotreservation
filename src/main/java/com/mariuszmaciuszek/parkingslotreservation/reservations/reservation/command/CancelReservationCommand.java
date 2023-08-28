package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command;

import com.mariuszmaciuszek.parkingslotreservation.reservations.command.Command;
import com.mariuszmaciuszek.parkingslotreservation.reservations.command.CommandDescriptor;
import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationCannotBeCanceledException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository.ParkingSlotRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class CancelReservationCommand
        implements Command<CancelReservationCommand.CancelReservationCommandDescriptor, Reservation> {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    @Override
    @Transactional
    public Optional<Reservation> run(@NotNull @Valid CancelReservationCommandDescriptor commandDescriptor) {
        validateUser(commandDescriptor.getUserId());
        final Reservation reservation = reservationRepository
                .findReservationByIdAndUserId(commandDescriptor.getReservationId(), commandDescriptor.getUserId())
                .orElseThrow(() -> new ReservationNotFoundException(commandDescriptor.getReservationId()));
        if (!reservation.canCancelReservation()) {
            throw new ReservationCannotBeCanceledException(commandDescriptor.getReservationId());
        }
        if (reservation.isOccupied()) {
            reservation.setOccupationEndedDate(DateTimeUtils.now());
        }
        reservation.cancelReservation();
        releaseParkingSlot(reservation);
        return Optional.ofNullable(reservationRepository.save(reservation));
    }

    private void releaseParkingSlot(Reservation reservation) {
        parkingSlotRepository
                .findParkingSlotById(reservation.getParkingSlotId())
                .ifPresent(parkingSlot -> {
                    parkingSlot.setAvailable();
                    parkingSlotRepository.save(parkingSlot);
                });
    }

    private void validateUser(UUID userId) {
        if (!userRepository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Builder
    @EqualsAndHashCode
    @ToString
    public static class CancelReservationCommandDescriptor implements CommandDescriptor {
        @NotNull
        private final UUID reservationId;

        @NotNull
        private final UUID userId;
    }
}
