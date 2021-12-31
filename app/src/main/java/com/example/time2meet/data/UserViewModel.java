package com.example.time2meet.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.time2meet.data.User;

import java.util.ArrayList;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    public UserViewModel() {
        this.userRepository = UserRepository.getInstance();
    }

    public Boolean login(String username, String password) {
        return userRepository.login(username, password);
    }

    public Integer signUp(String username, String password, String name, String dob, String phone) {
        return userRepository.signUp(username, password, name, dob, phone);
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

    public User getCurrentUser() {
        return userRepository.getUser().getValue();
    }

    public ArrayList<Meeting> getUserMeetingList() {
        return userRepository.getUserAllMeetings().getValue();
    }

    public ArrayList<Meeting> getUserUpComingMeeting() {
        ArrayList<Meeting> meetingList = new ArrayList<>();
        for (Meeting m: userRepository.getUserAllMeetings().getValue()) {
            if (m.getDate().length() > 0) {
                meetingList.add(m);
            }
        }
        return meetingList;
    }

    public Boolean isUsernameExist(String username) {
        return userRepository.isUsernameExist(username);
    }


    public Integer createMeeting(Integer hostID, String name, String startDate, String endDate, String date,
                                 String location, String description) {
        return userRepository.createMeeting(new Meeting(hostID, name, startDate, endDate,
                date, location, description));
    }

    public Boolean isMeetingExist(Integer meetingID) {
        return userRepository.isMeetingExist(meetingID);
    }
}
