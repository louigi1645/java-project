package com.penacony.hotel.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.penacony.hotel.entity.Room;
import com.penacony.hotel.repository.RoomRepository;


public class RoomController {

    // Inject the RoomRepository to interact with the database
    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Method to get all available rooms
    @GetMapping("/rooms/available")
    public String getAvailableRooms(Model model) {
        List<Room> availableRooms = roomRepository.findByIsAvailable(true).orElse(new ArrayList<>());
        model.addAttribute("availableRooms", availableRooms);
        return "rooms";
    }

    // Method to get room details by ID
    @GetMapping("/rooms/{id}")
    public String getRoomDetails(@PathVariable Long id, Model model) {
        Room room = roomRepository.findById(id).orElse(null);
        model.addAttribute("room", room);
        return "rooms";
    }
}
