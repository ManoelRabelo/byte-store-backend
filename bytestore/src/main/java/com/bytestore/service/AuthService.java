package com.bytestore.service;

import com.bytestore.dto.AuthRequestDTO;
import com.bytestore.dto.AuthResponseDTO;
import com.bytestore.dto.RegisterRequestDTO;
import com.bytestore.entity.User;
import com.bytestore.repository.UserRepository;
import com.bytestore.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return buildAuthResponse(user, token);
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);
        return buildAuthResponse(user, token);
    }

    @Transactional(readOnly = true)
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
