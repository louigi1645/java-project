package com.penacony.hotel.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.penacony.PenaconyApplication;
import com.penacony.hotel.entity.User;
import com.penacony.hotel.repository.UserRepository;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(PenaconyApplication.class.getName());
    private final UserRepository userRepository;
    

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + username));
        
        // Assurez-vous que les rôles sont correctement préfixés avec "ROLE_"
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    String roleName = role.getName().name();
                    // Vérifier si le nom du rôle commence déjà par "ROLE_"
                    return new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toList());
        
        logger.info("Utilisateur trouvé: " + user.getUsername());
        logger.info("Autorités: " + authorities);
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}