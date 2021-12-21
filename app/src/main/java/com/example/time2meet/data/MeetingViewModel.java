package com.example.time2meet.data;

import androidx.lifecycle.ViewModel;

public class MeetingViewModel extends ViewModel {
    private final MeetingRepository meetingRepository;

    public MeetingViewModel() {
        this.meetingRepository = MeetingRepository.getInstance();
    }

    public Integer goMeeting(Integer meetingID) {
        return meetingRepository.goMeeting(meetingID);
    }
}
