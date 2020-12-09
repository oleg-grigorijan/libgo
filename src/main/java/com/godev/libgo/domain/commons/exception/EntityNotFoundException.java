package com.godev.libgo.domain.commons.exception;

import java.util.UUID;

public class EntityNotFoundException extends ApplicationException {

    private EntityNotFoundException(String message) {
        super(message);
    }

    private EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static EntityNotFoundException byId(UUID id) {
        return new EntityNotFoundException("");
    }

    public static EntityNotFoundException byIdentity(String identity) {
        return new EntityNotFoundException("");
    }
}
