package com.example.time2meet.data;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.time2meet.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MeetingRepository {
    private static MeetingRepository instance;
    private MutableLiveData<Meeting> meeting = new MutableLiveData<Meeting>();
    public final Integer REQUEST_SUCCESS = 1;
    public final Integer REQUEST_ERROR = 2;
    public final Integer RESPONSE_ERROR = 3;
    protected final Integer RESPONSE_SUCCESS = 4;

    public Integer request_state;

    public static MeetingRepository getInstance(){
        if (instance == null) {
            instance = new MeetingRepository();
        }
        return instance;
    }

    private MeetingRepository() {}

    public LiveData<Meeting> getMeeting() {
        return meeting;
    }

    public Integer createMeeting(Meeting new_meeting) {
        try {
            Meeting result = new createMeetingAsyncTask().execute(new_meeting).get();
            if (request_state == REQUEST_SUCCESS) {
                UserRepository userRepository = UserRepository.getInstance();
                User host = userRepository.getUser().getValue();
                ArrayList<Integer> meetingList = host.getMeetingList();
                meetingList.add(result.getMeetingID());
                host.setMeetingList(meetingList);
                userRepository.updateMeetingList(host.getUsername(), meetingList);
                userRepository.setUser(host);
            }
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer goMeeting(Integer meetingID) {
        try {
            Meeting _meeting = new getMeetingAsyncTask().execute(meetingID).get();
            if (_meeting != null){
                meeting.setValue(_meeting);
                return RESPONSE_SUCCESS;
            }
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer inviteAttendee(String username) {
        UserRepository userRepository = UserRepository.getInstance();
        User _user = userRepository.getUser(username);
        ArrayList<Integer> meetingList = _user.getMeetingList();
        meetingList.add(meeting.getValue().getMeetingID());
        _user.setMeetingList(meetingList);
        userRepository.updateMeetingList(username, meetingList);

        Meeting _meeting = getMeeting().getValue();
        Map<Integer, ArrayList<Integer>> attendees = _meeting.getAttendees();
        attendees.put(_user.getUserID(), new ArrayList<>());
        _meeting.setAttendees(attendees);
        meeting.setValue(_meeting);
        try {
            new updateMeetingAsyncTask().execute(_meeting).get();
            return REQUEST_ERROR;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    //================================================================================
    //=================================AsyncTask======================================

    private class createMeetingAsyncTask extends AsyncTask<Meeting, Void, Meeting> {

        @Override
        protected Meeting doInBackground(Meeting... meetings) {
            try {
                Meeting response = ApiService.apiService.createMeeting(meetings[0])
                                            .execute().body();
                request_state = REQUEST_SUCCESS;
                return response;
            } catch (IOException e) {
                request_state=REQUEST_ERROR;
                e.printStackTrace();
            }
            return null;
        }
    }

    private class getMeetingAsyncTask extends AsyncTask<Integer, Void, Meeting> {

        @Override
        protected Meeting doInBackground(Integer... integers) {
            try {
                ArrayList<Meeting> response = ApiService.apiService.getMeeting(integers[0])
                                        .execute().body();
                request_state = REQUEST_SUCCESS;
                if (response.size() == 0) {
                    return null;
                }
                return response.get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            request_state = REQUEST_ERROR;
            return null;
        }
    }

    private class updateMeetingAsyncTask extends AsyncTask<Meeting, Void, Void> {

        @Override
        protected Void doInBackground(Meeting... meetings) {
            try {
                ApiService.apiService.updateMeeting(meetings[0].getMeetingID(), meetings[0])
                        .execute();
                request_state = REQUEST_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
                request_state = REQUEST_ERROR;
            }
            return null;
        }
    }
}
