package com.edfeff.auth.exception;

public class NeedPermException extends AuthException {
    public NeedPermException() {
    }

    public NeedPermException(String message) {
        super(message);
    }
}
