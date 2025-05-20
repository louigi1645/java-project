package com.penacony.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.penacony.hotel.entity.Reservation;
import com.penacony.hotel.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<List<Reservation>> findByUser(User user);

    Optional<List<Reservation>> findByCheckInDate(LocalDate checkInDate);

}