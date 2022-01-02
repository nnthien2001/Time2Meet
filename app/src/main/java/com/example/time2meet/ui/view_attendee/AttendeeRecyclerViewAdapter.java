package com.example.time2meet.ui.view_attendee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.time2meet.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendeeRecyclerViewAdapter extends RecyclerView.Adapter<AttendeeRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "AttendeeRecyclerViewAdapter";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mUsernames = new ArrayList<>();
    private Context mContext;

    public AttendeeRecyclerViewAdapter(Context mContext, ArrayList<String> mNames, ArrayList<String> mUsernames) {
        this.mNames = mNames;
        this.mUsernames = mUsernames;
        this.mContext = mContext;
    }

    public ArrayList<String> getmNames() {
        return mNames;
    }

    public ArrayList<String> getmUsernames() {
        return mUsernames;
    }

    public void setmNames(ArrayList<String> mNames) {
        this.mNames = mNames;
        notifyDataSetChanged();
    }

    public void setmUsernames(ArrayList<String> mUsernames) {
        this.mUsernames = mUsernames;
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
        holder.attendeeName.setText(mNames.get(position));
        holder.attendeeUsername.setText(mUsernames.get(position));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePicture;
        TextView attendeeName;
        TextView attendeeUsername;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            attendeeName = itemView.findViewById(R.id.attendee_name);
            attendeeUsername = itemView.findViewById(R.id.attendee_username);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
