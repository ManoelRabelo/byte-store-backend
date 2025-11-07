package com.bytestore.exception;

import java.util.UUID;

public class ProductNameAlreadyExistsException extends DuplicateResourceException {

    public ProductNameAlreadyExistsException(String productName) {
        super("Já existe um produto com o nome", productName);
    }

    public ProductNameAlreadyExistsException(String productName, UUID existingProductId) {
        super(String.format("Já existe outro produto com o nome '%s' (ID: %s).", productName, existingProductId));
    }
}

