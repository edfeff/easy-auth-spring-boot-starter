package com.edfeff.auth.user;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserService implements UserService {
    public Map<String, User> userMap = new HashMap<>();

    @Override
    public User getFromToken(String token) {
        return userMap.getOrDefault(token, User.Anon);
    }
}
