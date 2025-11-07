package com.bytestore.exception;

public class InsufficientStockException extends BaseException {

    public InsufficientStockException(String productName, Integer available, Integer requested) {
        super(String.format("Estoque insuficiente para o produto '%s'. Disponível: %d, Solicitado: %d",
                productName, available, requested));
    }

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}

