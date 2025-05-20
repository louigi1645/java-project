package com.penacony.hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.penacony.hotel.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<List<Room>> findByIsAvailable(boolean isAvailable);

    Optional<Room> findByRoomNumber(Long roomNumber);

    Optional<List<Room>> findByNumberOfBeds(int numberOfBeds);
}