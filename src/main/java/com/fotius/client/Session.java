package com.fotius.client;

import com.fotius.shared.model.User;

public class Session {

    public static User currentUser = null;

    public static void authorize(User user) {
        currentUser = user;
    }

}
