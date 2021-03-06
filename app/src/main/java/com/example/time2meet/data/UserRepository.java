package com.example.time2meet.data;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.time2meet.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    private static UserRepository instance;
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Meeting>> meetingList = new MutableLiveData<>();
    public final Integer REQUEST_SUCCESS = 1;
    public final Integer REQUEST_ERROR = 2;

    private Integer request_state;

    public static UserRepository getInstance(){
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private UserRepository() {}

    public LiveData<User> getUser() {
        return user;
    }
    public LiveData<ArrayList<Meeting>> getUserAllMeetings() { return meetingList; }
    public void setUser(User _user) {
        user.setValue(_user);
    }

    public User getUser(String username){
        try {
            return new GetUserAsyncTask().execute(username).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(Integer userID){
        try {
            return new GetUserByIDAsyncTask().execute(userID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean login(String username, String password){
        User _user = getUser(username);
        if (_user != null && _user.getPassword().equals(password)){
            user.setValue(_user);
            getAllMeetings();
            return true;
        }
        return false;
    }

    public Integer signUp(String username, String password, String name, String dob, String phone){
        try {
            new signUpAsyncTask().execute(username, password, name, dob, phone).get();
            return request_state;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer updateProfile(User new_user) {
        try {
            new updateUserAsyncTask().execute(new_user).get();
            if (request_state.equals(REQUEST_SUCCESS))
                user.setValue(new_user);
            return request_state;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer changePassword(String password) {
        User new_user = new User(user.getValue());
        new_user.setPassword(password);
        try{
            new updateUserAsyncTask().execute(new_user).get();
            if (request_state.equals(REQUEST_SUCCESS))
                user.setValue(new_user);
            return request_state;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();;
        }
        return REQUEST_ERROR;
    }

    public Integer updateMeetingList(String username, ArrayList<Integer> _meetingList) {

        try {
            User _user = new GetUserAsyncTask().execute(username).get();
            if (_user == null) {
                return null;
            }
            _user.setMeetingList(_meetingList);
            new updateUserAsyncTask().execute(_user).get();
            return request_state;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer getAllMeetings() {
        try {
            ArrayList<Meeting> all_meetings = new ArrayList<>();
            for (Integer i : user.getValue().getMeetingList()) {
                Meeting meeting = new getMeetingAsyncTask().execute(i).get();
                all_meetings.add(meeting);
            }
            Log.i("test", String.valueOf(all_meetings.size()));
            meetingList.setValue(all_meetings);
            return request_state;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer updateUser(User _user) {
        try {
            new updateUserAsyncTask().execute(_user).get();
            return request_state;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer createMeeting(Meeting new_meeting) {
        try {
            Meeting result = new createMeetingAsyncTask().execute(new_meeting).get();
            if (request_state == REQUEST_SUCCESS) {
                User host = user.getValue();
                ArrayList<Integer> meetingList = host.getMeetingList();
                meetingList.add(result.getMeetingID());
                host.setMeetingList(meetingList);
                updateMeetingList(host.getUsername(), meetingList);
                setUser(host);
                getAllMeetings();
            }
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Boolean isMeetingExist(Integer meetingID){
        try {
            Meeting result = new getMeetingAsyncTask().execute(meetingID).get();
            if (result == null){
                return false;
            }
            return true;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    //================================================================================
    //=================================AsyncTask======================================

    private class GetUserAsyncTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... strings) {
            try {
                ArrayList<User> response = ApiService.apiService.getUser(strings[0])
                        .execute()
                        .body();
                request_state = REQUEST_SUCCESS;
                if (response.size() > 0) {
                    return response.get(0);
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                request_state = REQUEST_ERROR;
            }
            return null;
        }
    }

    private class GetUserByIDAsyncTask extends AsyncTask<Integer, Void, User> {
        @Override
        protected User doInBackground(Integer... integers) {
            try {
                ArrayList<User> response = ApiService.apiService.getUser(integers[0])
                        .execute()
                        .body();
                request_state = REQUEST_SUCCESS;
                if (response.size() > 0) {
                    return response.get(0);
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                request_state = REQUEST_ERROR;
            }
            return null;
        }
    }

    private class getAllUsersAsyncTask extends AsyncTask<Void, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            try {
                ArrayList<User> response = ApiService.apiService.getAllUsers().execute().body();
                request_state = REQUEST_SUCCESS;
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                request_state = REQUEST_ERROR;
            }
            return null;
        }
    }

    private class signUpAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            User new_user = new User(strings[0], strings[1]);
            new_user.setName(strings[2]);
            new_user.setDob(strings[3]);
            new_user.setPhone(strings[4]);
            try {
                ApiService.apiService.createUser(new_user).execute();
                request_state = REQUEST_SUCCESS;
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            request_state = REQUEST_ERROR;
            return null;
        }
    }

    private class updateUserAsyncTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            try {
                ApiService.apiService.updateUser(users[0].getUserID(), users[0]).execute();
                request_state = REQUEST_SUCCESS;
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            request_state = REQUEST_ERROR;
            return null;
        }
    }

    private class getAllMeetingsAsyncTask extends AsyncTask<Integer, Void, ArrayList<Meeting>> {

        @Override
        protected ArrayList<Meeting> doInBackground(Integer... integers) {
            try {
                ArrayList<Meeting> all_meetings = ApiService.apiService.getMeetings(integers[0])
                                                    .execute().body();
                request_state = REQUEST_SUCCESS;
                return all_meetings;
            } catch (IOException e) {
                request_state = REQUEST_ERROR;
                e.printStackTrace();
            }
            return null;
        }
    }

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

    //================================================================================
    //==================================Constraints====================================
    public Boolean isUsernameExist(String username){
        try {
            ArrayList<User> users = new getAllUsersAsyncTask().execute().get();
            if (request_state.equals(REQUEST_ERROR)){
                return false;
            }
            for (int i = 0; i < users.size(); ++i){
                if (username.equals(users.get(i).getUsername())){
                    return true;
                }
            }
            return false;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
