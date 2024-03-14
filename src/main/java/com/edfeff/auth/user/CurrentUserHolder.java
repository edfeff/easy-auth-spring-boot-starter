package com.edfeff.auth.user;

public class CurrentUserHolder {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setUser(User user) {
        currentUser.set(user);
    }

    public static User getUser() {
        User user = currentUser.get();
        if (user == null) {
            return User.Anon;
        }
        return user;
    }

    public static void removeUser() {
        currentUser.remove();
    }
}
