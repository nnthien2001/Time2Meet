package com.example.time2meet.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MeetingViewModel extends ViewModel {
    private MeetingRepository meetingRepository;

    public MeetingViewModel() {
        meetingRepository = new MeetingRepository();
    }

//    public MeetingViewModel(Integer meetingID) {
//        meetingRepository = new MeetingRepository(meetingID);
//    }

    public Meeting getMeeting() {
        return meetingRepository.getMeeting().getValue();
    }

    public LiveData<Meeting> getMeetingLiveDate() {
        return meetingRepository.getMeeting();
    }

    public Integer goMeeting(Integer meetingID) {
        return meetingRepository.goMeeting(meetingID);
    }

    public Integer updateMeeting(String meetingName, String date, String location, String description) {
        return meetingRepository.updateMeeting(meetingName, date, location, description);
    }

    public  Integer toggleTime(ArrayList<Integer>interval){
        return meetingRepository.toggleTime(interval);
    }

    public ArrayList<User> getAttendees() {
        return meetingRepository.getAllAttendee().getValue();
    }

    public User getHost() {
        ArrayList<User> _attendees = meetingRepository.getAllAttendee().getValue();
        Meeting meeting = meetingRepository.getMeeting().getValue();
        for (User u : _attendees) {
//            Log.d("MVM", meeting.getHostID().toString());
//            if (u == null)
//                Log.d("MVM attendee", "is null");
//            Log.d("MVM", u.getName());

            if (u.getUserID().equals(meeting.getHostID()))
                return u;
        }
        return null;
    }

    public Integer invite(String username) {
        return meetingRepository.inviteAttendee(username);
    }

    public Integer removeAttendee(Integer userID) {
        return meetingRepository.removeAttendee(userID);
    }

    public Integer deleteMeeting() {
        return meetingRepository.deleteMeeting();
    }
}
