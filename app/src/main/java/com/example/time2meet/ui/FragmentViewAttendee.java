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

import com.example.time2meet.R;
import com.example.time2meet.data.MeetingViewModel;

import java.util.ArrayList;

public class FragmentViewAttendee extends Fragment {

    private NavController navController;
    private MeetingViewModel meetingViewModel;

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
    }

    void initRecyclerView(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.attendee_recycler_view);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> usernames = new ArrayList<>();

        //Get test data
        for(int i=0; i<10; i++) {
            names.add("TADA" + String.valueOf(i));
            usernames.add("tada" + String.valueOf(i));
        }
        AtendeeRecyclerViewAdapter adapter = new AtendeeRecyclerViewAdapter(getContext(), names, usernames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}