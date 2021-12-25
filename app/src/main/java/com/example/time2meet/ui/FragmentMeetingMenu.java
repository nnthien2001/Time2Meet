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

public class FragmentMeetingMenu extends Fragment implements View.OnClickListener {

    private NavController navController;
    private MeetingViewModel meetingViewModel;

    private EditText meeting_tv;

    public FragmentMeetingMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);

        meeting_tv = (EditText) view.findViewById(R.id.meeting_edittext);
        view.findViewById(R.id.edit_meeting_btn).setOnClickListener(this);
        view.findViewById(R.id.st_edit_meeting_btn).setOnClickListener(this);
        view.findViewById(R.id.view_attendee_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_meeting_btn) {
//            Meeting meeting = new Meeting(meeting_tv.getText().toString());
//
//            meetingViewModel.setMeeting(meeting);
//            navController.navigate(R.id.action_fragmentMeetingMenu_to_fragmentEditMeeting);
        }

        if (v.getId() == R.id.st_edit_meeting_btn) {
            navController.navigate(R.id.action_fragmentMeetingMenu_to_fragmentAvailabilityBoard);
        }

        if(v.getId() == R.id.view_attendee_btn) {
            navController.navigate(R.id.action_fragmentMeetingMenu_to_fragmentViewAttendee);
        }
    }
}