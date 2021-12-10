package com.example.time2meet.data;

import java.util.ArrayList;

public class User {
    private String username = "default";
    private Integer userID = 2;
    private String name = "default";
    private String dob = "default";
    private String password = "default";
    private String phone = "default";
    private ArrayList<Integer> meetingList;

    public User(String _username) {
        username = _username;
    }
    public User(User _user) {username = _user.getUsername();}

    public String getUsername() {
        return username;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Integer> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(ArrayList<Integer> meetingList) {
        this.meetingList = meetingList;
    }
}
