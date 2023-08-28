package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command;

import com.mariuszmaciuszek.parkingslotreservation.reservations.command.Command;
import com.mariuszmaciuszek.parkingslotreservation.reservations.command.CommandDescriptor;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationCannotBeActivatedException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.ReservationNotFoundException;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.UserNotFoundException;
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
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class ActivateReservationCommand
        implements Command<ActivateReservationCommand.ActivateCommandDescriptor, Reservation> {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<Reservation> run(
            @NotNull @Valid ActivateReservationCommand.ActivateCommandDescriptor commandDescriptor) {
        validateUser(commandDescriptor.getUserId());
        final Reservation reservation = reservationRepository
                .findReservationByIdAndUserId(commandDescriptor.getReservationId(), commandDescriptor.getUserId())
                .orElseThrow(() -> new ReservationNotFoundException(commandDescriptor.getReservationId()));
        if (!reservation.canActivateReservation()) {
            throw new ReservationCannotBeActivatedException(commandDescriptor.getReservationId());
        }
        reservation.activateReservation();
        return Optional.ofNullable(reservationRepository.save(reservation));
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
    public static class ActivateCommandDescriptor implements CommandDescriptor {
        @NotNull
        private final UUID reservationId;

        @NotNull
        private final UUID userId;
    }
}
