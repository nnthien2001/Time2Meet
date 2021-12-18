package com.example.time2meet.data;

public class Authenticator {
    public static final int ACCOUNT_SUCCESSFULLY_AUTHENTICATED = 1;
    public static final int NON_EXISTING_ACCOUNT_OR_WRONG_PASSWORD = 2;

    public Authenticator() {
        // Required empty public constructor
    }

    public int authenticate(User user) {
        return ACCOUNT_SUCCESSFULLY_AUTHENTICATED;
    }
}
