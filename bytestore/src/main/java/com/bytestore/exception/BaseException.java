package com.bytestore.exception;

public abstract class BaseException extends RuntimeException {

    private final String errorCode;

    protected BaseException(String message) {
        super(message);
        this.errorCode = this.getClass().getSimpleName();
    }

    protected BaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = this.getClass().getSimpleName();
    }

    protected BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

