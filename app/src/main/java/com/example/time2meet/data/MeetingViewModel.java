package com.example.time2meet.data;

import androidx.lifecycle.ViewModel;

public class MeetingViewModel extends ViewModel {
    private Meeting meeting = new Meeting("default");

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }
}
