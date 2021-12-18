package com.example.time2meet.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.time2meet.data.User;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    public UserViewModel() {
        this.userRepository = UserRepository.getInstance();
    }

    public Boolean login(String username, String password) {
        return userRepository.login(username, password);
    }

    public Integer signUp(String username, String password) {
        return userRepository.signUp(username, password);
    }

    public Integer updateProfile(String username, String name, String dob, String phone, String about) {
        User user = new User(userRepository.getUser().getValue());
        user.setUsername(username);
        user.setName(name);
        user.setDob(dob);
        user.setPhone(phone);
        user.setAbout(about);
        return userRepository.updateProfile(user);
    }

    public Integer changePassword(String password) {
        return userRepository.changePassword(password);
    }
}
