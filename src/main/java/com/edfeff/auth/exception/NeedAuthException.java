package com.edfeff.auth.exception;

public class NeedAuthException extends AuthException {
    public NeedAuthException() {
    }

    public NeedAuthException(String message) {
        super(message);
    }
}
