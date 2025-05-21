package com.penacony.hotel.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.penacony.hotel.entity.Client;
import com.penacony.hotel.entity.Reservation;
import com.penacony.hotel.entity.Room;
import com.penacony.hotel.entity.Role;
import com.penacony.hotel.entity.ERole;
import com.penacony.hotel.entity.User;
import com.penacony.hotel.repository.ClientRepository;
import com.penacony.hotel.repository.ReservationRepository;
import com.penacony.hotel.repository.RoomRepository;
import com.penacony.hotel.repository.UserRepository;
import com.penacony.hotel.service.UserService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final UserService userService;

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    public ReservationController(UserService userService, ReservationRepository reservationRepository,
            UserRepository userRepository, ClientRepository clientRepository, RoomRepository roomRepository) {
        this.userService = userService;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/new")
    public String showReservationForm(Model model) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(new Role(ERole.ROLE_ADMIN))) {
            return "redirect:/admin/reservations"; // Rediriger vers la page d'administration si l'utilisateur est un
                                                   // admin
        }
        Optional<List<Room>> availableRooms = roomRepository.findByIsAvailable(true); // Récupérer les chambres
        // disponibles
        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "Aucune chambre disponible pour la réservation."); // Ajouter un message
            // d'erreur
        } else {
            model.addAttribute("rooms", availableRooms.get()); // Ajouter les chambres disponibles au modèle
        }
        return "reservation_form"; // Retourner le nom de la vue du formulaire de réservation

    }

    @GetMapping
    public String viewReservations(Model model) {

        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(new Role(ERole.ROLE_ADMIN))) {
            return "redirect:/admin/reservations"; // Rediriger vers la page d'administration si l'utilisateur est un admin
        }
        Client client = clientRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("Client not found")); // Trouver le client associé à l'utilisateur
        List<Reservation> reservations = reservationRepository.findByUser(client)
                .orElseThrow(() -> new IllegalStateException("No reservations found for the user")); // Récupérer les réservations de l'utilisateur
        model.addAttribute("reservations", reservations); // Ajouter les réservations au modèle
        return "reservations";
    }

    @PostMapping
    public String createReservation(
            @RequestParam(name = "roomId") Long roomId,
            @RequestParam(name = "checkInDate") LocalDate checkInDate,
            @RequestParam(name = "checkOutDate") LocalDate checkOutDate,
            Model model) {

        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(new Role(ERole.ROLE_ADMIN))) {
            return "redirect:/admin/reservations"; // Rediriger vers la page d'administration si l'utilisateur est un admin
        }
        
        else if (roomRepository.existsById(roomId) && roomRepository.findById(roomId).get().getIsAvailable() == Boolean.TRUE) {
            Reservation reservation = new Reservation(roomId, checkInDate, checkOutDate);
            reservationRepository.save(reservation); // Enregistrer la réservation dans la base de données
        }
        return "redirect:/reservations";
    }
    @PostMapping("/cancel")
    public String deleteReservation(
            @RequestParam(name = "reservationId") Long reservationId,
            Model model) {
        // Logique pour supprimer une réservation
        if (reservationId != null){
            Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalStateException("Reservation not found"));
            reservationRepository.delete(reservation);
        }
        return "redirect:/reservations";
    }
}
