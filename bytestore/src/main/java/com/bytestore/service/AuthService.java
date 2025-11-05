package com.bytestore.service;

import com.bytestore.dto.AuthRequestDTO;
import com.bytestore.dto.AuthResponseDTO;
import com.bytestore.dto.RegisterRequestDTO;
import com.bytestore.entity.Role;
import com.bytestore.entity.User;
import com.bytestore.repository.UserRepository;
import com.bytestore.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return buildAuthResponse(user, token);
    }

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);
        return buildAuthResponse(user, token);
    }

    public boolean validateToken(String token) {
        String username = jwtService.extractUsername(token);
        if (username == null) return false;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return jwtService.validateToken(token, userDetails);
    }

    private AuthResponseDTO buildAuthResponse(User user, String token) {
        return new AuthResponseDTO(
                token,
                "Bearer",
                jwtService.extractExpiration(token).toInstant(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
