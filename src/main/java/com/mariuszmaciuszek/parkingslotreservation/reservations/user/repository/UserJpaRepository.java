package com.mariuszmaciuszek.parkingslotreservation.reservations.user.repository;

import com.mariuszmaciuszek.parkingslotreservation.reservations.user.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {}
