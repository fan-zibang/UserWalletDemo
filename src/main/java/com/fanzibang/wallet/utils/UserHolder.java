package com.fanzibang.wallet.utils;

import com.fanzibang.wallet.domain.User;

public class UserHolder {

    private static ThreadLocal<User> userThreadLocal =  new ThreadLocal<>();

    public static void set(User user) {
        userThreadLocal.set(user);
    }

    public static User get() {
        return userThreadLocal.get();
    }
}
