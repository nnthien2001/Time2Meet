package com.example.time2meet.ui.view_attendee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time2meet.R;
import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.User;
import com.example.time2meet.data.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewAttendee extends Fragment {

    private NavController navController;
    private MeetingViewModel meetingViewModel;
    private UserViewModel userViewModel;
    private View view;
    private User host;
    private FloatingActionButton fab;

    public FragmentViewAttendee() {
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
        View rootView = inflater.inflate(R.layout.fragment_view_attendee, container, false);

        initRecyclerView(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new MeetingViewModel();
        userViewModel = new UserViewModel();

        this.view = view;
        try {
            this.host = meetingViewModel.getHost();
        } catch (Exception e) {
            host = new User();
            host.setUserID(1);
        }

        initAppBar();
        fab = view.findViewById(R.id.fab);
        if(isHostOfThisMeeting()) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(getContext(), "Hello man", Toast.LENGTH_LONG);
                    toast.show();
                    // TODO: implement add a new attendee to list
                }
            });
        }

    }

    private void initRecyclerView(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.attendee_recycler_view);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();

        getData(names, usernames);
        AttendeeRecyclerViewAdapter adapter = new AttendeeRecyclerViewAdapter(getContext(), names, usernames);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //This is the code to provide a sectioned list
        List<AttendeeSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<AttendeeSectionedRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new AttendeeSectionedRecyclerViewAdapter.Section(0,"host"));
        if(names.size()>1) {
            sections.add(new AttendeeSectionedRecyclerViewAdapter.Section(1, "attendee"));
        }

        //Add your adapter to the sectionAdapter
        AttendeeSectionedRecyclerViewAdapter.Section[] dummy = new AttendeeSectionedRecyclerViewAdapter.Section[sections.size()];
        AttendeeSectionedRecyclerViewAdapter mSectionedAdapter = new
                AttendeeSectionedRecyclerViewAdapter(getContext(),R.layout.section,R.id.section_text,adapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        recyclerView.setAdapter(mSectionedAdapter);
    }

    private void getData(ArrayList<String> names, ArrayList<String> usernames) {
        try {
            ArrayList<User> attendees = meetingViewModel.getAttendees();
            attendees.remove(this.host);
            names.add(this.host.getName());
            usernames.add(this.host.getUsername());
            for(User attendee : attendees) {
                names.add(attendee.getName());
                usernames.add(attendee.getUsername());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            for(int i=0; i<10; i++) {
                names.add("This is test name:" + Integer.toString(i));
                usernames.add("This is test username" + Integer.toString(i));
            }
        }

    }

    private void initAppBar() {
        TextView tv_appbar = (TextView) view.findViewById(R.id.tv_action_bar_center);
        tv_appbar.setText("Attendance List");
        ImageButton imgBtn_back = (ImageButton) view.findViewById(R.id.btn_action_bar_leftmost);
        imgBtn_back.setImageResource(R.drawable.ic_back);
    }

    private boolean isHostOfThisMeeting() {
        try {
            return this.host.getUserID().equals(userViewModel.getCurrentUser().getUserID());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return true;
    }

}