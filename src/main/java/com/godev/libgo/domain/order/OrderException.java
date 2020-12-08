package com.godev.libgo.domain.order;

import com.godev.libgo.domain.commons.exception.ApplicationException;

public class OrderException extends ApplicationException {

    private OrderException(String message) {
        super(message);
    }

    private OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public static OrderException invalidStateChange() {
        return new OrderException("");
    }
}
