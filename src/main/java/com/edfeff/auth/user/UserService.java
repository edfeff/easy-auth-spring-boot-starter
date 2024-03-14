package com.edfeff.auth.user;

public interface UserService {
    User getFromToken(String token);
}
