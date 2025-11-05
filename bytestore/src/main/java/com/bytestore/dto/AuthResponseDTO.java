package com.bytestore.dto;

import com.bytestore.entity.Role;

import java.time.Instant;
import java.util.UUID;

public record AuthResponseDTO(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        UUID userId,
        String name,
        String email,
        Role role
) {
}
