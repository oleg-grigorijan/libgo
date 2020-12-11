package com.godev.libgo.domain.user;

import com.godev.libgo.domain.commons.exception.ApplicationException;
import com.godev.libgo.domain.commons.model.Email;

import static com.godev.libgo.MessageKeys.User.Error.ALREADY_EXISTS_BY_EMAIL;

public class UserException extends ApplicationException {

    private UserException(String message) {
        super(message);
    }

    private UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public static UserException alreadyExistsByEmail(Email email) {
        return new UserException(ALREADY_EXISTS_BY_EMAIL);
    }
}
