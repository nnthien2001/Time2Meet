package com.example.time2meet.data;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.time2meet.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MeetingRepository {
    //private static MeetingRepository instance;
    private MutableLiveData<Meeting> meeting = new MutableLiveData<Meeting>();
    private MutableLiveData<ArrayList<User>> attendees = new MutableLiveData<>();
    static public final Integer REQUEST_SUCCESS = 1;
    static public final Integer REQUEST_ERROR = 2;
    static public final Integer RESPONSE_ERROR = 3;
    protected final Integer RESPONSE_SUCCESS = 4;

    public Integer request_state;

//    public static MeetingRepository getInstance(){
//        if (instance == null) {
//            instance = new MeetingRepository();
//        }
//        return instance;
//    }

    public MeetingRepository() {}

//    public MeetingRepository(Integer meetingID) {
//        goMeeting(meetingID);
//    }

    //--------- Service ---------------

    public LiveData<Meeting> getMeeting() {
        return meeting;
    }

    public LiveData<ArrayList<User>> getAllAttendee() { return attendees; }

    public Integer goMeeting(Integer meetingID) {
        try {
            Meeting _meeting = new getMeetingAsyncTask().execute(meetingID).get();
            if (_meeting != null){
                meeting.setValue(_meeting);
                Set<Integer> attendeesID = _meeting.getAttendees().keySet();
                ArrayList<User> _attendees = new ArrayList<>();
                for (Integer i: attendeesID) {
                    User u = UserRepository.getInstance().getUser(i);
                    _attendees.add(u);
                }
                attendees.setValue(_attendees);
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
        if (null == _user || !_user.getUsername().equals(username)) {
            return REQUEST_ERROR;
        }
        ArrayList<Integer> meetingList = _user.getMeetingList();
        if (meetingList.contains(meeting.getValue().getMeetingID()))
            return REQUEST_SUCCESS;
        meetingList.add(meeting.getValue().getMeetingID());
        _user.setMeetingList(meetingList);
        userRepository.updateMeetingList(username, meetingList);

        Meeting _meeting = meeting.getValue();
        Map<Integer, ArrayList<Integer>> _attendees = _meeting.getAttendees();
        _attendees.put(_user.getUserID(), new ArrayList<>());
        _meeting.setAttendees(_attendees);
        meeting.setValue(_meeting);

        Set<Integer> attendeesID = _attendees.keySet();
        ArrayList<User> tmpAttendees = new ArrayList<>();
        for (Integer i: attendeesID) {
            User u = UserRepository.getInstance().getUser(i);
            tmpAttendees.add(u);
        }
        attendees.setValue(tmpAttendees);

        try {
            new updateMeetingAsyncTask().execute(_meeting).get();
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer removeAttendee(Integer userID) {
        Meeting _meeting = meeting.getValue();
        Map<Integer, ArrayList<Integer>> _attendeesID = _meeting.getAttendees();
        _attendeesID.remove(userID);
        _meeting.setAttendees(_attendeesID);
        try {
            new updateMeetingAsyncTask().execute(_meeting).get();
            User _user = null;
            ArrayList<User> _attendees = attendees.getValue();
            for (User u: _attendees) {
                if (u.getUserID().equals(userID)) {
                    _user = u;
                    break;
                }
            }

            ArrayList<Integer> meetingList = _user.getMeetingList();
            meetingList.remove(_meeting.getMeetingID());
            _user.setMeetingList(meetingList);
            UserRepository.getInstance().updateUser(_user);
            _attendees.remove(_user);
            attendees.setValue(_attendees);
            meeting.setValue(_meeting);
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer updateMeeting(String meetingName, String date, String location, String description) {
        Meeting _meeting = meeting.getValue();
        _meeting.setMeetingName(meetingName);
        _meeting.setDate(date);
        _meeting.setDescription(description);
        _meeting.setLocation(location);
        try {
            new updateMeetingAsyncTask().execute(_meeting).get();
            if (request_state == REQUEST_SUCCESS)
                meeting.setValue(_meeting);
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer toggleTime(ArrayList<Integer> newInterval) {
        Integer userID = UserRepository.getInstance().getUser().getValue().getUserID();
        Meeting _meeting = meeting.getValue();
        Map<Integer, ArrayList<Integer>> attendeeList = _meeting.getAttendees();
        attendeeList.replace(userID, newInterval);
        try {
            new updateMeetingAsyncTask().execute(_meeting).get();
            if (request_state == REQUEST_SUCCESS)
                meeting.setValue(_meeting);
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    //================================================================================
    //=================================AsyncTask======================================

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
