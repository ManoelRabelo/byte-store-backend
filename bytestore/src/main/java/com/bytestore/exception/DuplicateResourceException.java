package com.bytestore.exception;

public class DuplicateResourceException extends BaseException {

    public DuplicateResourceException(String resourceName, String identifier) {
        super(String.format("%s '%s' já existe no sistema.", resourceName, identifier));
    }

    public DuplicateResourceException(String message) {
        super(message);
    }
}

