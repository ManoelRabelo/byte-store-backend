package com.bytestore.exception;

import java.util.UUID;

public class OrderCancelledException extends BaseException {

    public OrderCancelledException(UUID orderId) {
        super(String.format("Não é possível realizar operações no pedido %s pois ele foi cancelado.", orderId));
    }

    public OrderCancelledException(String message) {
        super(message);
    }
}

