package com.example.time2meet.data;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.time2meet.ApiService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

public class UserRepository {
    private static UserRepository instance;
    private MutableLiveData<User> user = new MutableLiveData<User>();
    public final Integer REQUEST_SUCCESS = 1;
    public final Integer REQUEST_ERROR = 2;
    public final Integer RESPONSE_ERROR = 3;
    protected final Integer RESPONSE_SUCCESS = 4;

    public Integer request_state;

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

    public Integer login(String username, String password){
        try {
            User _user = new GetUserAsyncTask().execute(username).get();
            if (request_state == REQUEST_ERROR) {
                return request_state;
            }

            if (!_user.getPassword().equals(password)){
                return RESPONSE_ERROR;
            }
            user.setValue(_user);
            return RESPONSE_SUCCESS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer signUp(String username, String password){
        try {
            new signUpAsyncTask().execute(username, password).get();
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer updateProfile(String username, String name, String dob, String phone, String about) {
        User new_user = new User(user.getValue());
        new_user.setUsername(username=="" ? new_user.getUsername() : username);
        new_user.setName(name=="" ? new_user.getName() : name);
        new_user.setDob(dob=="" ? new_user.getDob() : dob);
        new_user.setPhone(phone=="" ? new_user.getPhone() : phone);
        new_user.setAbout(about=="" ? new_user.getAbout() : about);
        try {
            new updateUserAsyncTask().execute(new_user).get();
            if (request_state == REQUEST_SUCCESS)
                user.setValue(new_user);
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
    }

    public Integer changePassword(String password) {
        User new_user = new User(user.getValue());
        new_user.setPassword(password);
        try{
            new updateUserAsyncTask().execute(new_user).get();
            if (request_state == REQUEST_SUCCESS)
                user.setValue(new_user);
            return request_state;
        } catch (ExecutionException e) {
            e.printStackTrace();;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return REQUEST_ERROR;
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

    //================================================================================
    //==================================Helper Functions==============================
    public Boolean isUsernameExist(String username){
        try {
            ArrayList<User> users = new getAllUsersAsyncTask().execute().get();
            if (request_state == REQUEST_ERROR){
                return false;
            }
            for (int i = 0; i < users.size(); ++i){
                if (username.equals(users.get(i).getUsername())){
                    return true;
                }
            }
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isValidDate (String value){
        LocalDateTime ldt = null;
        String format = "dd/MM/yyyy";
        Locale locale = Locale.ENGLISH;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, formatter);
            String result = ldt.format(formatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, formatter);
                String result = ld.format(formatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, formatter);
                    String result = lt.format(formatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    e2.printStackTrace();
                }
            }
        }

        return false;
    }
}
