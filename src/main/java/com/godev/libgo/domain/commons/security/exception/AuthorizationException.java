package com.godev.libgo.domain.commons.security.exception;

import com.godev.libgo.infra.security.BaseAuthority;

import static com.godev.libgo.MessageKeys.Security.Error.NO_AUTHORITY;

public class AuthorizationException extends SecurityException {

    private AuthorizationException(String message) {
        super(message);
    }

    private AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AuthorizationException noAuthority(BaseAuthority authority) {
        return new AuthorizationException(NO_AUTHORITY);
    }
}
