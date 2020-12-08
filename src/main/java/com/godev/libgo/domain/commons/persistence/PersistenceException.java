package com.godev.libgo.domain.commons.persistence;

import com.godev.libgo.domain.commons.exception.ApplicationException;

public class PersistenceException extends ApplicationException {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public static PersistenceException noTransactionContext() {
        return new PersistenceException("");
    }

    public static PersistenceException cantConnect(Throwable cause) {
        return new PersistenceException("", cause);
    }

    public static PersistenceException unexpected(Throwable cause) {
        return new PersistenceException("", cause);
    }
}
