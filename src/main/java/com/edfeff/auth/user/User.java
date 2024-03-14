package com.edfeff.auth.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public interface User {
    String name();

    Collection<String> roles();

    Collection<String> perms();

    Map<String, Object> attrs();

    User Anon = new DefaultUser("anon", new HashSet<>(), new HashSet<>());

    class DefaultUser implements User {
        private String name;
        private Collection<String> roles;
        private Collection<String> perms;
        private Map<String, Object> attrs = new HashMap<>();

        public DefaultUser(String name, Collection<String> roles, Collection<String> perms) {
            this.name = name;
            this.roles = roles;
            this.perms = perms;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Collection<String> roles() {
            return roles;
        }

        @Override
        public Collection<String> perms() {
            return perms;
        }

        @Override
        public Map<String, Object> attrs() {
            return attrs;
        }
    }
}
