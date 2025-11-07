package com.bytestore.exception;

import java.util.UUID;

public class OrderNotFoundException extends ResourceNotFoundException {

    public OrderNotFoundException(UUID orderId) {
        super("Pedido", orderId);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}

