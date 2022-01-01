package com.example.time2meet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.databinding.FragmentEditMeetingBinding;

public class FragmentEditMeeting extends Fragment {

    private FragmentEditMeetingBinding binding;
    private NavController navController;
    private MeetingViewModel meetingViewModel;
    private Meeting meeting;
    private Integer meetingID;

    public FragmentEditMeeting() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);

        // set up meeting
        meetingViewModel.goMeeting(meetingID);
        meeting = meetingViewModel.getMeeting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditMeetingBinding.inflate(inflater, container, false);
        //return inflater.inflate(R.layout.fragment_edit_meeting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        navController = Navigation.findNavController(view);
//
//        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
//        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);

        // set up binding view
        binding.meetingEditName.setText(meeting.getMeetingName());
        binding.meetingEditTime.setText(meeting.getDate());
        binding.meetingEditLocation.setText(meeting.getLocation());
        binding.meetingEditDescription.setText(meeting.getDescription());

        binding.meetingBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.meetingEditName.getText().toString();
                String time = binding.meetingEditTime.getText().toString();
                String location = binding.meetingEditLocation.getText().toString();
                String description = binding.meetingEditDescription.getText().toString();

                meetingViewModel.updateMeeting(name, time, location, description);
                navController.navigateUp();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}