package com.bytestore.exception;

import java.util.UUID;

public class OrderAccessDeniedException extends BaseException {

    public OrderAccessDeniedException(UUID orderId) {
        super(String.format("Você não tem permissão para acessar o pedido %s.", orderId));
    }

    public OrderAccessDeniedException(String message) {
        super(message);
    }
}

