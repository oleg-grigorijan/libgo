package com.godev.libgo.domain.user;

import com.godev.libgo.domain.commons.exception.ApplicationException;
import com.godev.libgo.domain.commons.model.Email;

public class UserException extends ApplicationException {

    private UserException(String message) {
        super(message);
    }

    private UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UserException alreadyExistsByEmail(Email email) {
        return new UserException("");
    }
}
