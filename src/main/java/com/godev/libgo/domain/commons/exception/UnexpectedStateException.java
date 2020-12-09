package com.godev.libgo.domain.commons.exception;

public class UnexpectedStateException extends ApplicationException {

    public UnexpectedStateException(String message) {
        super(message);
    }

    private UnexpectedStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
