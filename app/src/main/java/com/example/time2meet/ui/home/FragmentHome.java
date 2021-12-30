package com.example.time2meet.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time2meet.R;
import com.example.time2meet.data.Helper;
import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.data.User;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FragmentHome extends Fragment implements View.OnClickListener{

    private NavController navController;
    private UserViewModel userViewModel;
    private MeetingViewModel meetingViewModel;
    private LayoutInflater inflater;
    private View createMeetingPopupView;
    private Helper helper;
    private PopupWindow createMeetingPopup;
    public FragmentHome() {
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
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph);
        userViewModel = new ViewModelProvider(backStackEntry).get(UserViewModel.class);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);
//        welcomeTextSetup(view, user);
        buttonsSetup(view);
        //tabLayoutSetup(view);
        meetingListSetup(view);
        helper = Helper.getInstance();
    }

    private void welcomeTextSetup(@NonNull View view, User user) {
        String message = "Welcome " + user.getUsername();
//        TextView tv = (TextView) view.findViewById(R.id.welcome_textview);
//        tv.setText(message);
    }

    private void buttonsSetup(@NonNull View view) {
//        view.findViewById(R.id.btn_meeting).setOnClickListener(this);
        view.findViewById(R.id.btn_action_bar_leftmost).setOnClickListener(this);
        ImageButton profile_btn = (ImageButton)view.findViewById(R.id.btn_action_bar_rightmost);
        profile_btn.setOnClickListener(this);
        profile_btn.setImageResource(this.getResources()
                .getIdentifier(userViewModel.getCurrentUser().getImage(), "drawable", this.getContext().getPackageName()));
        view.findViewById(R.id.btn_create_meeting).setOnClickListener(this);
    }

    private void tabLayoutSetup(@NonNull View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_home);
        tabLayout.addTab(tabLayout.newTab().setText("All Meetings"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming Meetings"));
    }

    private void meetingListSetup(View view) {
        RecyclerView rvMeetings = (RecyclerView) getView().findViewById(R.id.rv_meetings);

        // TODO: Handle get meeting list logic
        ArrayList<Meeting> meetings = new ArrayList<Meeting>();

        MeetingListAdapter adapter = new MeetingListAdapter(meetings);
        rvMeetings.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_meeting:
//                navController.navigate(R.id.action_fragmentHome_to_fragmentMeetingMenu);
//                break;
            case R.id.btn_create_meeting:
                openCreateNewMeeting(v);
                break;
            case R.id.btn_action_bar_leftmost:
                break;
            case R.id.btn_action_bar_rightmost:
                navController.navigate(R.id.action_fragmentHome_to_fragmentProfile);
                break;
            case R.id.btn_save_meeting:
                createMeeting();
                break;

        }
    }

    void openCreateNewMeeting(View view) {
        View layout = inflater.inflate(R.layout.popup_create_meeting, null);
        PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        createMeetingPopup = popupWindow;
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        createMeetingPopupView = popupWindow.getContentView();
        ImageButton btn_left = (ImageButton) createMeetingPopupView.findViewById(R.id.btn_action_bar_leftmost);
        btn_left.setImageResource(0);
        TextView tv_mid = (TextView) createMeetingPopupView.findViewById(R.id.tv_action_bar_center);
        tv_mid.setText("Create meeting");
        ImageButton btn_right = (ImageButton) createMeetingPopupView.findViewById(R.id.btn_action_bar_rightmost);
        btn_right.setImageResource(0);
        createMeetingPopupView.findViewById(R.id.btn_save_meeting).setOnClickListener(this);
    }

    void createMeeting() {
        String meeting_name = ((EditText)createMeetingPopupView.findViewById(R.id.edit_meeting_name))
                .getText().toString();
        String meeting_start_date = ((EditText) createMeetingPopupView.findViewById(R.id.edit_meeting_startdate))
                .getText().toString();
        String meeting_end_date = ((EditText) createMeetingPopupView.findViewById(R.id.edit_meeting_enddate))
                .getText().toString();
        String meeting_date = ((EditText) createMeetingPopupView.findViewById(R.id.edit_meeting_date))
                .getText().toString();
        String location = ((EditText) createMeetingPopupView.findViewById(R.id.edit_meeting_location))
                .getText().toString();
        String description = ((EditText) createMeetingPopupView.findViewById(R.id.edit_meeting_description))
                .getText().toString();
        if (meeting_name.length()==0 || meeting_start_date.length()==0 || meeting_end_date.length()==0) {
            Toast.makeText(this.getContext(), "Please enter enough information for a meeting",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!helper.isValidDate(meeting_start_date) || ! helper.isValidDate(meeting_end_date) ||
        helper.compareDate(meeting_start_date, meeting_end_date) > 0){
            Toast.makeText(this.getContext(), "Please input date as dd/mm/yyyy and " +
                    "make sure that start date is smaller than end date", Toast.LENGTH_SHORT).show();
        }
        else {
            meetingViewModel.createMeeting(userViewModel.getCurrentUser().getUserID(),
                    meeting_name, meeting_start_date, meeting_end_date, meeting_date, location,description);
            createMeetingPopup.dismiss();
        }
    }
}