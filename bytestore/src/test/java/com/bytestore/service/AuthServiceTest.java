package com.bytestore.service;

import com.bytestore.dto.AuthRequestDTO;
import com.bytestore.dto.AuthResponseDTO;
import com.bytestore.dto.RegisterRequestDTO;
import com.bytestore.entity.Role;
import com.bytestore.entity.User;
import com.bytestore.exception.DuplicateResourceException;
import com.bytestore.exception.UserNotFoundException;
import com.bytestore.repository.UserRepository;
import com.bytestore.security.jwt.JwtService;
import com.bytestore.util.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    // ========== REGISTER ==========

    @Test
    void should_registerUser_when_validData() {
        RegisterRequestDTO request = TestDataBuilder.registerRequestDTO();
        User savedUser = TestDataBuilder.user()
                .email(request.email())
                .name(request.name())
                .role(request.role())
                .build();
        String token = "jwt-token";
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(jwtService.extractExpiration(token)).thenReturn(expirationDate);

        AuthResponseDTO result = authService.register(request);

        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo(token);
        assertThat(result.tokenType()).isEqualTo("Bearer");
        assertThat(result.email()).isEqualTo(request.email());
        assertThat(result.name()).isEqualTo(request.name());
        assertThat(result.role()).isEqualTo(request.role());
        verify(userRepository).findByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
        verify(jwtService).extractExpiration(token);
    }

    @Test
    void should_encodePassword_when_registeringUser() {
        RegisterRequestDTO request = TestDataBuilder.registerRequestDTO();
        User savedUser = TestDataBuilder.user()
                .email(request.email())
                .build();
        String encodedPassword = "encodedPassword123";
        String token = "jwt-token";
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(jwtService.extractExpiration(token)).thenReturn(expirationDate);

        authService.register(request);

        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(argThat(user -> 
            user.getPassword().equals(encodedPassword) &&
            !user.getPassword().equals(request.password())
        ));
    }

    @Test
    void should_throwDuplicateResourceException_when_emailAlreadyExists() {
        RegisterRequestDTO request = TestDataBuilder.registerRequestDTO();
        User existingUser = TestDataBuilder.user()
                .email(request.email())
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Usuário")
                .hasMessageContaining(request.email())
                .hasMessageContaining("já existe");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void should_registerUserWithAdminRole_when_roleIsAdmin() {
        RegisterRequestDTO request = TestDataBuilder.registerRequestDTO(
                "Admin User",
                "admin@teste.com",
                "senha123",
                Role.ADMIN
        );
        User savedUser = TestDataBuilder.user()
                .email(request.email())
                .name(request.name())
                .role(Role.ADMIN)
                .build();
        String token = "jwt-token";
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(jwtService.extractExpiration(token)).thenReturn(expirationDate);

        AuthResponseDTO result = authService.register(request);

        assertThat(result.role()).isEqualTo(Role.ADMIN);
        verify(userRepository).save(argThat(user -> user.getRole() == Role.ADMIN));
    }

    // ========== AUTHENTICATE ==========

    @Test
    void should_authenticateUser_when_validCredentials() {
        AuthRequestDTO request = TestDataBuilder.authRequestDTO();
        User user = TestDataBuilder.user()
                .email(request.email())
                .build();
        String token = "jwt-token";
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(jwtService.extractExpiration(token)).thenReturn(expirationDate);

        AuthResponseDTO result = authService.authenticate(request);

        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo(token);
        assertThat(result.email()).isEqualTo(request.email());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(request.email());
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void should_throwBadCredentialsException_when_invalidCredentials() {
        AuthRequestDTO request = TestDataBuilder.authRequestDTO();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Email ou senha incorretos");

        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void should_throwUserNotFoundException_when_userNotFoundAfterAuthentication() {
        AuthRequestDTO request = TestDataBuilder.authRequestDTO();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(request.email());

        verify(jwtService, never()).generateToken(any());
    }

    // ========== VALIDATE TOKEN ==========

    @Test
    void should_returnTrue_when_validToken() {
        String token = "valid-token";
        String username = "user@teste.com";
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        boolean result = authService.validateToken(token);

        assertThat(result).isTrue();
        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtService).validateToken(token, userDetails);
    }

    @Test
    void should_returnFalse_when_usernameIsNull() {
        String token = "invalid-token";

        when(jwtService.extractUsername(token)).thenReturn(null);

        boolean result = authService.validateToken(token);

        assertThat(result).isFalse();
        verify(jwtService).extractUsername(token);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).validateToken(anyString(), any());
    }

    @Test
    void should_returnFalse_when_tokenIsInvalid() {
        String token = "invalid-token";
        String username = "user@teste.com";
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(false);

        boolean result = authService.validateToken(token);

        assertThat(result).isFalse();
        verify(jwtService).validateToken(token, userDetails);
    }
}

