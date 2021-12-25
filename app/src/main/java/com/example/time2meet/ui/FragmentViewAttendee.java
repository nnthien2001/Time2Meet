package com.example.time2meet.ui;

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

import com.example.time2meet.R;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.UserViewModel;

import java.util.ArrayList;

public class FragmentViewAttendee extends Fragment {

    private NavController navController;
    private MeetingViewModel meetingViewModel;
    private UserViewModel userViewModel;
    private View view;

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
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);
        userViewModel = new UserViewModel();

        this.view = view;
        initAppBar();
    }

    private void initRecyclerView(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.attendee_recycler_view);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();

        getData(names, usernames);
        AttendeeRecyclerViewAdapter adapter = new AttendeeRecyclerViewAdapter(getContext(), names, usernames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getData(ArrayList<String> names, ArrayList<String> usernames) {
        // TODO: Get the attendance list from the meeting view model
        for(int i=0; i<10; i++) {
            names.add("TADA" + String.valueOf(i));
            usernames.add("tada" + String.valueOf(i));
        }
    }

    private void initAppBar() {
        TextView tv_appbar = (TextView) view.findViewById(R.id.tv_action_bar_center);
        tv_appbar.setText("Availability Board");
        ImageButton imgBtn_back = (ImageButton) view.findViewById(R.id.btn_action_bar_leftmost);
        imgBtn_back.setImageResource(R.drawable.ic_back);
    }

}