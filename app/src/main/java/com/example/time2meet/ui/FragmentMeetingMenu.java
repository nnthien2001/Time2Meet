package com.example.time2meet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.time2meet.R;
import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.databinding.FragmentMeetingMenuBinding;

public class FragmentMeetingMenu extends Fragment {

    private FragmentMeetingMenuBinding binding;
    private NavController navController;
    private MeetingViewModel meetingViewModel;
    private Meeting meeting;
    private Integer meetingID;

    public FragmentMeetingMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meetingID = getArguments().getInt("meetingID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMeetingMenuBinding.inflate(inflater, container, false);

        //return inflater.inflate(R.layout.fragment_meeting_menu, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);

        // set up meeting
        meetingViewModel.goMeeting(meetingID);
        meeting = meetingViewModel.getMeeting();

        // binding view
        binding.meetingStartDate.setText(meeting.getStartDate());
        binding.meetingEndDate.setText(meeting.getEndDate());
        if (meeting.getDate() != null) {
            binding.meetingProposedTime.setText(meeting.getDate());
        }
        binding.meetingLocation.setText(meeting.getLocation());
        binding.meetingHost.setText(meetingViewModel.getHost().getName());
        binding.meetingMeetingId.setText(meeting.getMeetingID());
        binding.meetingDescription.setText(meeting.getDescription());

        binding.meetingBtnViewAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentMeetingMenu_to_fragmentViewAttendee);
            }
        });
        binding.meetingBtnViewAvailability.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentMeetingMenu_to_fragmentAvailabilityBoard);
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}