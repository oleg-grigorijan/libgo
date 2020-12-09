package com.godev.libgo.domain.commons.security.exception;

public class AuthenticationException extends SecurityException {

    private AuthenticationException(String message) {
        super(message);
    }

    private AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
