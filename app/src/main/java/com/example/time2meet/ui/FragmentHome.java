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
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.data.User;
import com.google.android.material.tabs.TabLayout;

public class FragmentHome extends Fragment implements View.OnClickListener{

    private NavController navController;
    private UserViewModel userViewModel;

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph);
        userViewModel = new ViewModelProvider(backStackEntry).get(UserViewModel.class);
        User user = userViewModel.getUser();

        welcomeTextSetup(view, user);
        buttonsSetup(view);
        tabLayoutSetup(view);
    }

    private void welcomeTextSetup(@NonNull View view, User user) {
        String message = "Welcome " + user.getUsername();
        TextView tv = (TextView) view.findViewById(R.id.welcome_textview);
        tv.setText(message);
    }

    private void buttonsSetup(@NonNull View view) {
        view.findViewById(R.id.btn_meeting).setOnClickListener(this);
        view.findViewById(R.id.btn_action_bar_leftmost).setOnClickListener(this);
        view.findViewById(R.id.btn_action_bar_rightmost).setOnClickListener(this);
    }

    private void tabLayoutSetup(@NonNull View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_home);
        tabLayout.addTab(tabLayout.newTab().setText("All Meetings"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming Meetings"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_meeting:
                navController.navigate(R.id.action_fragmentHome_to_fragmentMeetingMenu);
                break;
            case R.id.btn_action_bar_leftmost:
                break;
            case R.id.btn_action_bar_rightmost:
                navController.navigate(R.id.action_fragmentHome_to_fragmentProfile);
                break;
        }
    }
}