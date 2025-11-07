package com.bytestore.exception;

import com.bytestore.entity.OrderStatus;

public class InvalidOrderStatusException extends BaseException {

    public InvalidOrderStatusException(OrderStatus currentStatus, String operation) {
        super(String.format("Não é possível %s um pedido com status '%s'.", operation, currentStatus));
    }

    public InvalidOrderStatusException(String message) {
        super(message);
    }
}

