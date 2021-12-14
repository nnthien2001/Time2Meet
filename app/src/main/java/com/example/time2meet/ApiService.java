package com.example.time2meet;

import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://61683c33ba841a001727c664.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("api/user")
    Call<ArrayList<User>> getUser(@Query("username") String username);

    @GET("api/user")
    Call<ArrayList<User>> getAllUsers();

    @POST("api/user")
    Call<User> createUser(@Body User user);

    @PUT("api/user/{id}")
    Call<User> updateUser(@Path("id") Integer userID, @Body User user);

    @DELETE("api/user/{id}")
    Call<User> deleteUser(@Path("id") Integer userID);

    @GET("api/meeting")
    Call<ArrayList<Meeting>> getMeetings(@Query("hostID") Integer hostID);

    @GET("api/meeting")
    Call<ArrayList<Meeting>> getMeeting(@Query("meetingID") Integer meetingID);

    @POST("api/meeting")
    Call<Meeting> createMeeting(@Body Meeting meeting);

    @PUT("api/meeting/{id}")
    Call<Meeting> updateMeeting(@Path("id") Integer meetingID, @Body Meeting meeting);

    @DELETE ("api/meeting/{id}")
    Call<Meeting> deleteMeeting(@Path("id") Integer meetingID);
}
