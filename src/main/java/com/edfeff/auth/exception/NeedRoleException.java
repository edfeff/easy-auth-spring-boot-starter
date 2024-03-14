package com.edfeff.auth.exception;

public class NeedRoleException extends AuthException {
    public NeedRoleException() {
    }

    public NeedRoleException(String message) {
        super(message);
    }
}
