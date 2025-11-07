package com.bytestore.exception;

import java.util.UUID;

public class ProductNotFoundException extends ResourceNotFoundException {

    public ProductNotFoundException(UUID productId) {
        super("Produto", productId);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}

