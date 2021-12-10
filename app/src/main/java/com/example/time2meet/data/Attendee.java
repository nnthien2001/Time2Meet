package com.example.time2meet.data;

import java.util.ArrayList;

public class Attendee {
    private String username;
    private String name;
    private ArrayList<Integer> available;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getAvailable() {
        return available;
    }

    public void setAvailable(ArrayList<Integer> available) {
        this.available = available;
    }

    public Attendee(String username, String name){
        this.username = username;
        this.name = name;
        available = new ArrayList<>();
    }
}
