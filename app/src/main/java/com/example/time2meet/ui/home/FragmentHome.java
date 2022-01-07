package com.example.time2meet.ui.home;

import android.graphics.Color;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time2meet.R;
import com.example.time2meet.data.DatePickerHelper;
import com.example.time2meet.data.Helper;
import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.data.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FragmentHome extends Fragment implements View.OnClickListener{

    private NavController navController;
    private UserViewModel userViewModel;
    private LayoutInflater inflater;
    private View createMeetingPopupView;
    private View joinMeetingPopupView;
    private Helper helper;
    private PopupWindow createMeetingPopup;
    private PopupWindow joinMeetingPopup;
    private MeetingListAdapter adapter;
    private ArrayList<Meeting> meetings;

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
        meetings = new ArrayList<>();
//        welcomeTextSetup(view, user);
        meetingListSetup();
        buttonsSetup(view);
        //tabLayoutSetup(view);
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
        view.findViewById(R.id.btn_join_meeting).setOnClickListener(this);
        FloatingActionButton fab_create=view.findViewById(R.id.btn_create_meeting);
        fab_create.setColorFilter(Color.argb(255,255,255,255));
        FloatingActionButton fab_join=view.findViewById(R.id.btn_join_meeting);
        fab_join.setColorFilter(Color.argb(255,255,255,255));

        ((TabLayout) view.findViewById(R.id.tab_home)).addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getText().toString()){
                    case "All meetings":
                        meetings.clear();
                        meetings.addAll(userViewModel.getUserMeetingList());
                        adapter.notifyDataSetChanged();

                        break;
                    case "Upcoming meetings":
                        meetings.clear();
                        meetings.addAll(userViewModel.getUserUpComingMeeting());
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void tabLayoutSetup(@NonNull View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_home);
        tabLayout.addTab(tabLayout.newTab().setText("All Meetings"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming Meetings"));
    }

    private void meetingListSetup() {
        RecyclerView rvMeetings = (RecyclerView) getView().findViewById(R.id.rv_meetings);

        // TODO: Handle get meeting list logic
        meetings.addAll(userViewModel.getUserMeetingList());
        adapter = new MeetingListAdapter(meetings, navController);
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
            case R.id.btn_join_meeting:
                openJoinMeeting(v);
                break;
            case R.id.btn_cancel_join:
                joinMeetingPopup.dismiss();
                break;
            case R.id.btn_join:
                joinMeeting();
                break;
            case R.id.tab_left:
                meetings.clear();
                meetings.addAll(userViewModel.getUserMeetingList());
                adapter.notifyDataSetChanged();
                break;
            case R.id.tab_right:
                meetings.clear();
                meetings.addAll(userViewModel.getUserUpComingMeeting());
                adapter.notifyDataSetChanged();
                Log.i("debug", String.valueOf(meetings.size()));
                break;
        }
    }

    private void joinMeeting() {
        Integer meetingID = Integer.parseInt(String.valueOf(((EditText)joinMeetingPopupView
                .findViewById(R.id.edit_meeting_ID))
                .getText()));
        if (!userViewModel.isMeetingExist(meetingID)) {
            Toast.makeText(this.getContext(), "This meeting does not exist", Toast.LENGTH_SHORT).show();
        }
        else {
            joinMeetingPopup.dismiss();
            // TODO: pass metingID to view meeting
            Bundle bundle = new Bundle();
            bundle.putInt("meetingID", meetingID);
            navController.navigate(R.id.action_fragmentHome_to_meeting_nav_graph, bundle);
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
        btn_left.setColorFilter(Color.argb(255,255,255,255));

        TextView tv_mid = (TextView) createMeetingPopupView.findViewById(R.id.tv_action_bar_center);
        tv_mid.setText(getResources().getString(R.string.create_meeting));
        tv_mid.setGravity(Gravity.LEFT);
        tv_mid.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.action_bar_text_size));

        ImageButton btn_right = (ImageButton) createMeetingPopupView.findViewById(R.id.btn_action_bar_rightmost);
        btn_right.setImageResource(0);
        btn_right.setColorFilter(Color.argb(255,255,255,255));
        createMeetingPopupView.findViewById(R.id.btn_save_meeting).setOnClickListener(this);

        // set up date picker
        DatePickerHelper datePickerHelper = new DatePickerHelper(getContext(), createMeetingPopupView);
        datePickerHelper.datePickerSetup((TextView) createMeetingPopupView.findViewById(R.id.edit_meeting_startdate));
        datePickerHelper.datePickerSetup((TextView) createMeetingPopupView.findViewById(R.id.edit_meeting_enddate));
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
            userViewModel.createMeeting(userViewModel.getCurrentUser().getUserID(),
                    meeting_name, meeting_start_date, meeting_end_date, meeting_date, location,description);
            createMeetingPopup.dismiss();
            meetings.clear();
            meetings.addAll(userViewModel.getUserMeetingList());
            adapter.notifyDataSetChanged();
        }
    }

    void openJoinMeeting(View view) {
        View layout = inflater.inflate(R.layout.popup_join_meeting, null);
        PopupWindow popupWindow = new PopupWindow(layout, (int) (300 * this.getContext().getResources().getDisplayMetrics().density),
                (int) (200 * this.getContext().getResources().getDisplayMetrics().density),
                true);
        joinMeetingPopup = popupWindow;
        joinMeetingPopupView = popupWindow.getContentView();
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        joinMeetingPopupView.findViewById(R.id.btn_cancel_join).setOnClickListener(this);
        joinMeetingPopupView.findViewById(R.id.btn_join).setOnClickListener(this);
    }
}