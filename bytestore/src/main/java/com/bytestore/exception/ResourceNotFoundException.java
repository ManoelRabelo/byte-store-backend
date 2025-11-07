package com.bytestore.exception;

import java.util.UUID;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceName, UUID id) {
        super(String.format("%s com ID %s não encontrado.", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s '%s' não encontrado.", resourceName, identifier));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

