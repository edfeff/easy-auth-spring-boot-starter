package com.edfeff.auth.exception;

import org.springframework.http.ResponseEntity;

public interface AuthExceptionHandler {
    ResponseEntity<?> handleAuthException(AuthException exception);
}
