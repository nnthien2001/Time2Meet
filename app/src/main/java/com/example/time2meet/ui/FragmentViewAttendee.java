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

import com.example.time2meet.R;
import com.example.time2meet.data.MeetingViewModel;

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
        return inflater.inflate(R.layout.fragment_view_attendee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);
    }
}