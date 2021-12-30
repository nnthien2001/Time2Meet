package com.example.time2meet.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time2meet.R;
import com.example.time2meet.data.Meeting;

import java.util.ArrayList;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.ViewHolder> {
    private ArrayList<Meeting> displayMeetings;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeetingName;
        TextView tvMeetingDate;
        TextView tvMeetingLocation;

        public ViewHolder(View view) {
            super(view);
            tvMeetingName = (TextView) view.findViewById(R.id.tv_meeting_name);
            tvMeetingDate = (TextView) view.findViewById(R.id.tv_meeting_date);
            tvMeetingLocation = (TextView) view.findViewById(R.id.tv_meeting_location);
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
    }

    public MeetingListAdapter(ArrayList<Meeting> meetings) {
        displayMeetings = meetings;
        Log.i("debug", String.valueOf(meetings.size()));
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
    }

    @Override
    public int getItemCount() {
        return displayMeetings.size();
    }
}