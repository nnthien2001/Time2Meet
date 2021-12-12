package com.example.time2meet.data;

import android.content.ContentResolver;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.time2meet.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static UserRepository instance;
    private MutableLiveData<User> user = new MutableLiveData<User>();

    public static UserRepository getInstance(){
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private UserRepository() {
        if (user == null || user.getValue() == null) {
            //user.setValue(...);
            // có thể set user trong đây
        }
        //hoặc ngoài đây
    }

    public LiveData<User> getUser() {
        return user;
    }

    public String Login(String username, String password) {
        return null;
    }
}
