package com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.command;

import com.mariuszmaciuszek.parkingslotreservation.reservations.command.Command;
import com.mariuszmaciuszek.parkingslotreservation.reservations.command.CommandDescriptor;
import com.mariuszmaciuszek.parkingslotreservation.reservations.common.validation.constraint.DateNotFromPast;
import com.mariuszmaciuszek.parkingslotreservation.reservations.exception.*;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parking.repository.ParkingRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlot;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.model.ParkingSlotSize;
import com.mariuszmaciuszek.parkingslotreservation.reservations.parkingslot.repository.ParkingSlotRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.Reservation;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.model.ReservationState;
import com.mariuszmaciuszek.parkingslotreservation.reservations.reservation.repository.ReservationRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository.UserRepository;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.CarSize;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository.UserCarRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
public class CreateReservationCommand
        implements Command<CreateReservationCommand.CreateReservationCommandDescriptor, Reservation> {
    private final @NotNull UserRepository userRepository;
    private final @NotNull UserCarRepository userCarRepository;
    private final @NotNull ParkingRepository parkingRepository;
    private final @NotNull ParkingSlotRepository parkingSlotRepository;
    private final @NotNull ReservationRepository reservationRepository;

    @Override
    @Transactional
    public Optional<Reservation> run(@Valid @NotNull CreateReservationCommandDescriptor commandDescriptor) {
        log.info("run({})", commandDescriptor);
        validateUser(commandDescriptor.getUserId());
        validateParkingExistence(commandDescriptor.getParkingId());
        validateStartAndEndTimePeriod(commandDescriptor.getStartDate(), commandDescriptor.getEndDate());
        checkIfCarHasActiveReservation(commandDescriptor.getUserCarId());

        final UserCar userCar = userCarRepository
                .findUserCarById(commandDescriptor.getUserCarId())
                .orElseThrow(() -> new UserCarNotFoundException(commandDescriptor.getUserCarId()));
        final UUID parkingSlotId = findParkingSlotAndMarkAsUnavailable(commandDescriptor, userCar.getCarSize());
        final Reservation createdReservation =
                reservationRepository.save(createReservation(commandDescriptor, parkingSlotId));
        return Optional.ofNullable(createdReservation);
    }

    private void checkIfCarHasActiveReservation(UUID userCarId) {
        if (reservationRepository.hasUserCarIdActiveReservation(
                userCarId, Reservation.IN_PROGRESS_RESERVATION_STATES)) {
            throw new ReservationCreationException.CarHasActiveReservationException(userCarId);
        }
    }

    private void validateParkingExistence(UUID parkingId) {
        if (!parkingRepository.parkingExists(parkingId)) {
            throw new ParkingNotFoundException(parkingId);
        }
    }

    private void validateStartAndEndTimePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ReservationPeriodMismatchException();
        }
    }

    private void validateUser(UUID userId) {
        if (!userRepository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private UUID findParkingSlotAndMarkAsUnavailable(
            CreateReservationCommandDescriptor commandDescriptor, CarSize carSize) {
        final UUID parkingId = commandDescriptor.getParkingId();
        final ParkingSlot parkingSlot =
                switch (carSize) {
                    case COMPACT -> smallSpot(parkingId)
                            .or(() -> mediumSpot(parkingId).or(() -> largeSpot(parkingId)))
                            .orElseThrow(() -> new ParkingSlotNotAvailableException(parkingId));
                    case MEDIUM -> mediumSpot(parkingId)
                            .or(() -> largeSpot(parkingId))
                            .orElseThrow(() -> new ParkingSlotNotAvailableException(parkingId));
                    case LARGE -> largeSpot(parkingId)
                            .orElseThrow(() -> new ParkingSlotNotAvailableException(parkingId));
                };
        parkingSlot.setUnavailable();
        return parkingSlotRepository.save(parkingSlot).getId();
    }

    private Optional<ParkingSlot> smallSpot(UUID parkingId) {
        return parkingSlotRepository.findAvailableParkingSlot(parkingId, ParkingSlotSize.SMALL);
    }

    private Optional<ParkingSlot> mediumSpot(UUID parkingId) {
        return parkingSlotRepository.findAvailableParkingSlot(parkingId, ParkingSlotSize.MEDIUM);
    }

    private Optional<ParkingSlot> largeSpot(UUID parkingId) {
        return parkingSlotRepository.findAvailableParkingSlot(parkingId, ParkingSlotSize.LARGE);
    }

    private Reservation createReservation(CreateReservationCommandDescriptor taskDescriptor, UUID parkingSlotId) {
        final Reservation reservationToPersist = new Reservation();
        reservationToPersist.setId(taskDescriptor.getReservationId());
        reservationToPersist.setUserId(taskDescriptor.getUserId());
        reservationToPersist.setUserCarId(taskDescriptor.getUserCarId());
        reservationToPersist.setParkingSlotId(parkingSlotId);
        reservationToPersist.setState(ReservationState.RESERVED);
        reservationToPersist.setStartedDate(taskDescriptor.getStartDate());
        reservationToPersist.setEndedDate(taskDescriptor.getEndDate());
        return reservationToPersist;
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Builder
    @EqualsAndHashCode
    @ToString
    public static class CreateReservationCommandDescriptor implements CommandDescriptor {
        @Builder.Default
        private final UUID reservationId = UUID.randomUUID();

        private final @NotNull UUID userId;
        private final @NotNull UUID userCarId;
        private final @NotNull UUID parkingId;
        private final @DateNotFromPast LocalDateTime startDate;
        private final @DateNotFromPast LocalDateTime endDate;
    }
}
