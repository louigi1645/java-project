package com.penacony;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.logging.Logger;
import com.penacony.hotel.entity.ERole;
import com.penacony.hotel.entity.Role;
import com.penacony.hotel.repository.RoleRepository;
import com.penacony.hotel.repository.UserRepository;
import com.penacony.hotel.service.UserService;


@SpringBootApplication
public class PenaconyApplication {
    private static final Logger logger = Logger.getLogger(PenaconyApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(PenaconyApplication.class, args);
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            logger.info("Initialisation des rôles...");
            logger.info("Nombre de rôles existants: " + roleRepository.count());

            // Initialiser les rôles si la table est vide
            if (roleRepository.count() == 0) {
                logger.info("Création du rôle ROLE_USER");
                roleRepository.save(new Role(ERole.ROLE_USER));

                logger.info("Création du rôle ROLE_ADMIN");
                roleRepository.save(new Role(ERole.ROLE_ADMIN));

                logger.info("Rôles initialisés avec succès. Nombre de rôles: " + roleRepository.count());
            } else {
                logger.info("Les rôles existent déjà. Vérification...");

                // Vérifier si les rôles existent
                boolean userRoleExists = roleRepository.findByName(ERole.ROLE_USER).isPresent();
                boolean adminRoleExists = roleRepository.findByName(ERole.ROLE_ADMIN).isPresent();

                logger.info("ROLE_USER existe: " + userRoleExists);
                logger.info("ROLE_ADMIN existe: " + adminRoleExists);

                // Créer les rôles manquants si nécessaire
                if (!userRoleExists) {
                    logger.info("Création du rôle ROLE_USER manquant");
                    roleRepository.save(new Role(ERole.ROLE_USER));
                }

                if (!adminRoleExists) {
                    logger.info("Création du rôle ROLE_ADMIN manquant");
                    roleRepository.save(new Role(ERole.ROLE_ADMIN));
                }
            }
        };
    }

    @Bean
    CommandLineRunner initAdmin(UserService userService, UserRepository userRepository) {
        return args -> {
            String username = "admin";
            String password = "MyS3cur3P@ssw0rd";
            logger.info("Initialisation des utilisateurs...");
            logger.info("Nombre d'utilisateurs existants: " + userService.count());

            // Initialiser les rôles si la table est vide

            if (userService.count() == 0) {
                logger.info("Création de l'employé admin");
                
                userService.registerNewEmployee(username, "", password, "", "","","","",
                null, 200000.0);

                logger.info("Création du compte admin");

            } else {
                logger.info("Les comptes existent déjà. Vérification...");

                // Vérifier si l'admin existe
                boolean adminExists = userRepository.existsByUsername(username);

                logger.info("l'employé existe: " + adminExists);

                // Créer les utilisateurs manquants si nécessaire
                if (!adminExists) {
                    logger.info("Création de l'admin manquant");
                    userService.registerNewEmployee(username, "", password, "", "", "", "", "",
                            null, 200000.0);
                } else {
                    logger.info("L'admin existe déjà");
                }
            }
            // Promouvoir l'utilisateur admin s'il n'est pas déjà admin
            if (!userRepository.findByUsername(username).get().getRoles().contains(new Role(ERole.ROLE_ADMIN))) {
                logger.info("L'utilisateur n'est pas admin, promotion...");
                userService.promoteToAdmin(userRepository.findByUsername(username).get().getId());
                logger.info("L'utilisateur a été promu avec succès");
            } else {
                logger.info("L'utilisateur est déjà admin");
            }
        };
    }
}