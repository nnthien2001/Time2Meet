package com.example.time2meet.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.time2meet.data.User;

public class UserViewModel extends ViewModel {
    private User user;

    public void setUser(User _user) {
        user = _user;
    }

    public User getUser() {
        return user;
    }
}
