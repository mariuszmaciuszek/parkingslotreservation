package com.mariuszmaciuszek.parkingslotreservation.reservations.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

public interface Command<CommandDescriptor, RS> {

    Optional<RS> run(@NotNull @Valid CommandDescriptor commandDescriptor);
}
