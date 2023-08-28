package com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.usercar.model.UserCar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCarsJpaRepository extends JpaRepository<UserCar, UUID> {
    List<UserCar> findAllByUserId(UUID userId);

    Optional<UserCar> findByIdAndUserId(UUID id, UUID userId);
}
