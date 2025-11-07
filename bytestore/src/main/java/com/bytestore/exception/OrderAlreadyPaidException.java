package com.bytestore.exception;

import java.util.UUID;

public class OrderAlreadyPaidException extends BaseException {

    public OrderAlreadyPaidException(UUID orderId) {
        super(String.format("O pedido %s já foi pago e não pode ser modificado.", orderId));
    }

    public OrderAlreadyPaidException(String message) {
        super(message);
    }
}

