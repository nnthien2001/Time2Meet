package com.example.time2meet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.databinding.FragmentViewMeetingBinding;

public class FragmentViewMeeting extends Fragment {

    private FragmentViewMeetingBinding binding;
    private NavController navController;
    private UserViewModel userViewModel;
    private MeetingViewModel meetingViewModel;
    private Meeting meeting;
    private Integer meetingID;

    public FragmentViewMeeting() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            meetingID = getArguments().getInt("meetingID");
            //Log.d("ViewMeeting mID", meetingID.toString());
        }

        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);
        userViewModel = new ViewModelProvider(navController.getBackStackEntry(R.id.nav_graph)).get(UserViewModel.class);

        // set up meeting
        meetingViewModel.goMeeting(meetingID);
        meeting = meetingViewModel.getMeeting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewMeetingBinding.inflate(inflater, container, false);

//        return inflater.inflate(R.layout.fragment_meeting_menu, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);
        userViewModel = new ViewModelProvider(navController.getBackStackEntry(R.id.nav_graph)).get(UserViewModel.class);

        //Log.d("ViewMeeting current meeting", meetingViewModel.getMeeting().getHostID().toString());
        //Log.d("ViewMeeting current user", userViewModel.getCurrentUser().getUserID().toString());

        setDataToView();

        binding.meetingBtnViewAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentMeetingMenu_to_fragmentViewAttendee);
            }
        });
        binding.meetingBtnViewAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("meetingID", meetingID);
                navController.navigate(R.id.action_fragmentMeetingMenu_to_fragmentAvailabilityBoard, bundle);
            }
        });

        setupAppBar();
        setupObserver();
    }

    public void setDataToView() {
        binding.meetingName.setText(meeting.getMeetingName());
        binding.meetingStartDate.setText(meeting.getStartDate());
        binding.meetingEndDate.setText(meeting.getEndDate());
        if (meeting.getDate() != null) {
            binding.meetingProposedTime.setText(meeting.getDate());
        }
        else {
            binding.meetingProposedTime.setText(R.string.meeting_time_undecided);
        }
        binding.meetingLocation.setText(meeting.getLocation());
        binding.meetingHost.setText(meetingViewModel.getHost().getName());
        binding.meetingMeetingId.setText(meeting.getMeetingID().toString());
        binding.meetingDescription.setText(meeting.getDescription());
    }

    public void setupObserver() {
        // set up observer
        final Observer<Meeting> meetingObserver = new Observer<Meeting>() {
            @Override
            public void onChanged(Meeting newMeeting) {
                setDataToView();
            }
        };
        meetingViewModel.getMeetingLiveDate().observe(getViewLifecycleOwner(), meetingObserver);
    }

    private void setupAppBar(){
        TextView appbar_title = getView().findViewById(R.id.tv_action_bar_center);
        appbar_title.setText(getResources().getString(R.string.meeting));
        appbar_title.setGravity(Gravity.START);
        appbar_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.action_bar_text_size));

        ImageButton back_button =getView().findViewById(R.id.btn_action_bar_leftmost);
        ImageButton edit_button =getView().findViewById(R.id.btn_action_bar_rightmost);
        back_button.setImageResource(R.drawable.ic_back);
        edit_button.setImageResource(R.drawable.ic_edit);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_fragmentMeetingMenu_to_fragmentEditMeeting);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigateUp();
            }
        });

        if (! userViewModel.getCurrentUser().getUserID().equals(meeting.getHostID())) {
            edit_button.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
        meetingViewModel.getMeetingLiveDate().removeObservers(getViewLifecycleOwner());
    }
}