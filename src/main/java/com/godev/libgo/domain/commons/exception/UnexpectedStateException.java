package com.godev.libgo.domain.commons.exception;

public class UnexpectedStateException extends ApplicationException {

    private UnexpectedStateException(String humanReadableMessage) {
        super(humanReadableMessage);
    }

    public static UnexpectedStateException because(String humanReadableMessage) {
        return new UnexpectedStateException(humanReadableMessage);
    }

    public static UnexpectedStateException notImplemented() {
        return UnexpectedStateException.because("Not implemented");
    }
}
