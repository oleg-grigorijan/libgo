package com.godev.libgo.domain.commons.security.exception;

import com.godev.libgo.domain.commons.security.Authority;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AuthorizationException extends SecurityException {

    private AuthorizationException(String message) {
        super(message);
    }

    private AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AuthorizationException noAuthority(Authority authority) {
        return new AuthorizationException("");
    }

    public static AuthorizationException userAuthRequired(UUID requiredUserId, @Nullable UUID actualUserId) {
        return new AuthorizationException("");
    }
}
