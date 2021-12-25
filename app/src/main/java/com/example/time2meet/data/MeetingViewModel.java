package com.example.time2meet.data;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MeetingViewModel extends ViewModel {
    private Meeting meeting;
    private MeetingRepository meetingRepository;

    public MeetingViewModel() {
        meetingRepository = MeetingRepository.getInstance();
    }

    public Meeting getMeeting() {
        return meetingRepository.getMeeting().getValue();
    }

    public Integer goMeeting(Integer meetingID) {
        return meetingRepository.goMeeting(meetingID);
    }

    public Integer toggleTime(ArrayList<Integer> interval){
        return meetingRepository.toggleTime(interval);
    }
}
