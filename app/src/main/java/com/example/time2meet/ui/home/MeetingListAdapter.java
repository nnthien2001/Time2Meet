package com.example.time2meet.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time2meet.R;
import com.example.time2meet.data.Meeting;

import java.util.ArrayList;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.ViewHolder> {
    private ArrayList<Meeting> displayMeetings;
    protected static NavController navController;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMeetingName;
        TextView tvMeetingDate;
        TextView tvMeetingLocation;
        RelativeLayout item;
        Integer meetingID;

        public ViewHolder(View view) {
            super(view);
            tvMeetingName = (TextView) view.findViewById(R.id.tv_meeting_name);
            tvMeetingDate = (TextView) view.findViewById(R.id.tv_meeting_date);
            tvMeetingLocation = (TextView) view.findViewById(R.id.tv_meeting_location);
            item = view.findViewById(R.id.meeting_item);
            item.setOnClickListener(this);
        }

        public TextView getTvMeetingName() {
            return tvMeetingName;
        }

        public TextView getTvMeetingDate() {
            return tvMeetingDate;
        }

        public TextView getTvMeetingLocation() {
            return tvMeetingLocation;
        }

        public void setMeetingID(Integer id) {
            this.meetingID = id;
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putInt("meetingID", meetingID);
            Log.i("debug", meetingID.toString());
            navController.navigate(R.id.action_fragmentHome_to_meeting_nav_graph, bundle);
        }
    }

    public MeetingListAdapter(ArrayList<Meeting> meetings, NavController navController) {
        displayMeetings = meetings;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.meeting_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting currentMeeting = displayMeetings.get(position);
        holder.getTvMeetingName().setText(currentMeeting.getMeetingName());
        holder.getTvMeetingDate().setText(currentMeeting.getDate());
        holder.getTvMeetingLocation().setText(currentMeeting.getLocation());
        holder.setMeetingID(currentMeeting.getMeetingID());
    }

    @Override
    public int getItemCount() {
        return displayMeetings.size();
    }
}