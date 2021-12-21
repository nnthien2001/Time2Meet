package com.example.time2meet.data;

import androidx.lifecycle.ViewModel;

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
}
