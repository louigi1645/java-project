package com.penacony.hotel.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.penacony.hotel.entity.*;
import com.penacony.hotel.repository.ClientRepository;
import com.penacony.hotel.repository.EmployeeRepository;
import com.penacony.hotel.repository.RoleRepository;
import com.penacony.hotel.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            ClientRepository clientRepository,
            EmployeeRepository employeeRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void validateUserCredentials(String username, String email) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(username))) {
            throw new RuntimeException("Erreur: Ce nom d'utilisateur est déjà pris!");
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            throw new RuntimeException("Erreur: Cet email est déjà utilisé!");
        }
    }

    private Set<Role> getUserRoles() {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle non trouvé."));
        roles.add(userRole);
        return roles;
    }

    public Client registerNewClient(String username, String email, String password,
            String firstName, String lastName, String phoneNumber,
            String address, LocalDate dateOfBirth) {
        validateUserCredentials(username, email);

        Client client = new Client();
        client.setUsername(username);
        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(password));
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setPhoneNumber(phoneNumber);
        client.setAddress(address);
        client.setDateOfBirth(dateOfBirth);
        client.setLoyaltyPoints(0);
        client.setRoles(getUserRoles());
        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            throw new RuntimeException("Erreur: Cet identifiant d'utilisateur est déjà utilisé!");
        }

        return clientRepository.save(client);
    }

    public Employee registerNewEmployee(String username, String email, String password,
            String firstName, String lastName, String employeeId,
            String department, String position, LocalDate hireDate,
            Double salary) {
        validateUserCredentials(username, email);

        if (employeeRepository.findByEmployeeId(employeeId).isPresent()) {
            throw new RuntimeException("Erreur: Cet identifiant d'employé est déjà utilisé!");
        }

        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmployeeId(employeeId);
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setHireDate(hireDate);
        employee.setSalary(salary);

        // Par défaut, les employés ont le rôle USER
        Set<Role> roles = getUserRoles();
        employee.setRoles(roles);

        return employeeRepository.save(employee);
    }

    // Méthode pour ajouter le rôle ADMIN à un employé
    public Employee promoteToAdmin(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'ID: " + employeeId));

        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Erreur: Rôle ADMIN non trouvé."));

        Set<Role> roles = employee.getRoles();
        roles.add(adminRole);
        employee.setRoles(roles);

        return employeeRepository.save(employee);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Récupérer le nom d'utilisateur de l'utilisateur connecté
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found")); // Trouver l'utilisateur connecté
    }

    public int count() {
        return (int) userRepository.count();
    }

}
