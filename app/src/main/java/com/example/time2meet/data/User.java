package com.example.time2meet.data;

public class User {
    private String username = "default";

    public User(String _username) {
        username = _username;
    }
    public User(User _user) {username = _user.getUsername();}

    public String getUsername() {
        return username;
    }
}
