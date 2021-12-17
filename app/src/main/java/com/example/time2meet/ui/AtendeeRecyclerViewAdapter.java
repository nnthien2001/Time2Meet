package com.example.time2meet.ui;

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

public class AtendeeRecyclerViewAdapter extends RecyclerView.Adapter<AtendeeRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "AtendeeRecyclerViewAdapter";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mUsernames = new ArrayList<>();
    private Context mContext;

    public AtendeeRecyclerViewAdapter(Context mContext, ArrayList<String> mNames, ArrayList<String> mUsernames) {
        this.mNames = mNames;
        this.mUsernames = mUsernames;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.atendee_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.atendeeName.setText(mNames.get(position));
        holder.atendeeUsername.setText(mUsernames.get(position));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePicture;
        TextView atendeeName;
        TextView atendeeUsername;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            atendeeName = itemView.findViewById(R.id.atendee_name);
            atendeeUsername = itemView.findViewById(R.id.atendee_username);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
