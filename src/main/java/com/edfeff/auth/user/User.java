package com.edfeff.auth.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class User {
    private String name;
    private Collection<String> roles;
    private Collection<String> perms;
    private Map<String, Object> attrs = new HashMap<>();

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public User(String name, Collection<String> roles, Collection<String> perms) {
        this.name = name;
        this.roles = roles;
        this.perms = perms;
    }

    public String getName() {
        return name;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public Collection<String> getPerms() {
        return perms;
    }

    public static final User Anon = new User("anon", new HashSet<>(), new HashSet<>());
}
