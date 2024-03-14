package com.edfeff.auth.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private final AuthExceptionHandler authExceptionHandler;

    public ExceptionControllerAdvice(AuthExceptionHandler authExceptionHandler) {
        this.authExceptionHandler = authExceptionHandler;
    }

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<?> handAuthException(AuthException exception) {
        return authExceptionHandler.handleAuthException(exception);
    }
}
