package com.bytestore.exception;

public class ValidationException extends BaseException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String message) {
        super(String.format("Erro de validação no campo '%s': %s", field, message));
    }
}

