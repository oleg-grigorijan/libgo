package com.godev.libgo.domain.commons.security.exception;

import com.godev.libgo.domain.commons.exception.ApplicationException;

public abstract class SecurityException extends ApplicationException {

    protected SecurityException(String message) {
        super(message);
    }

    protected SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
