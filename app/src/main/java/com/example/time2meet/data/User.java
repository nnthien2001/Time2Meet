package com.example.time2meet.data;

import java.util.ArrayList;

public class User {
    private String username = "default";
    private Integer userID;
    private String name = "default";
    private String dob = "default";
    private String password = "default";
    private String phone = "default";
    private ArrayList<Integer> meetingList;
    private String about = "default";
    private String image = "default";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(User _user) {
        username = _user.getUsername();
        userID = _user.getUserID();
        name = _user.getName();
        dob = _user.getDob();
        password = _user.getPassword();
        phone = _user.getPhone();
        meetingList = _user.getMeetingList();
        about = _user.getAbout();
        image = _user.getImage();
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
