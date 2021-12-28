package com.example.time2meet.data;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MeetingViewModel extends ViewModel {
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

    public ArrayList<User> getAttendees() {
        return meetingRepository.getAllAttendee().getValue();
    }

    public User getHost() {
        ArrayList<User> _attendees = meetingRepository.getAllAttendee().getValue();
        Meeting meeting = meetingRepository.getMeeting().getValue();

        for (User u : _attendees) {
            if (u.getUserID().equals(meeting.getHostID()))
                return u;
        }
        return null;
    }
}
