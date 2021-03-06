package com.example.time2meet.ui.view_attendee;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time2meet.R;
import com.example.time2meet.data.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendeeRecyclerViewAdapter extends RecyclerView.Adapter<AttendeeRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "AttendeeRecyclerViewAdapter";
    private ArrayList<User> users = new ArrayList<>();
    private Context mContext;
    static protected NavController navController;

    public AttendeeRecyclerViewAdapter(Context mContext, ArrayList<User> users, NavController nav) {
        this.users.addAll(users);
        this.mContext = mContext;
        navController = nav;
    }

    public User getUserByPosition(int position) {
        return users.get(position);
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendee_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.attendeeName.setText(users.get(position).getName());
        holder.attendeeUsername.setText(users.get(position).getUsername());
        holder.userID = users.get(position).getUserID();

        String avatar_id=users.get(position).getImage();
        int avatar_src= mContext.getResources().getIdentifier(avatar_id,"drawable",mContext.getPackageName());
        holder.profilePicture.setImageResource(avatar_src);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView profilePicture;
        TextView attendeeName;
        TextView attendeeUsername;
        RelativeLayout parentLayout;
        int userID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            attendeeName = itemView.findViewById(R.id.attendee_name);
            attendeeUsername = itemView.findViewById(R.id.attendee_username);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Go to user profile with userID " + Integer.toString(userID));
            Bundle bundle = new Bundle();
            bundle.putInt("userID", this.userID);
            navController.navigate(R.id.action_fragmentViewAttendee_to_fragmentProfile, bundle);
        }
    }
}
