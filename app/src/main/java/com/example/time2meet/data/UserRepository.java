package com.example.time2meet.data;

import android.content.ContentResolver;
import android.util.Log;

import com.example.time2meet.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static UserRepository instance;
    private static User user;

    public static UserRepository getInstance(){
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public String Login(String username, String password) {
        return null;
    }
}
