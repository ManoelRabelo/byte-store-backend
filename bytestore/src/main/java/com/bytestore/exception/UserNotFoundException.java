package com.bytestore.exception;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(UUID userId) {
        super("Usuário", userId);
    }

    public UserNotFoundException(String email) {
        super("Usuário", email);
    }
}

