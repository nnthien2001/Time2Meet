package com.example.time2meet.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.time2meet.data.User;

public class UserViewModel extends ViewModel {
    private LiveData<User> user;
    private final UserRepository userRepository;

    public UserViewModel() {
        this.userRepository = UserRepository.getInstance();
        // nên set user dưới repo
        // user.setValue(userRepository.getUser());
    }

    public User getUser() {
        return user.getValue();
    }
}
