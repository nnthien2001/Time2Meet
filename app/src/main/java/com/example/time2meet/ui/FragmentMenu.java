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

public class FragmentMenu extends Fragment implements View.OnClickListener{

    private NavController navController;
    private UserViewModel userViewModel;

    public FragmentMenu() {
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
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph);
        userViewModel = new ViewModelProvider(backStackEntry).get(UserViewModel.class);

//        User user = userViewModel.getUser();
//        String message = "Welcome " + user.getUsername();
//        TextView tv = (TextView) view.findViewById(R.id.welcome_textview);
//        tv.setText(message);
//
//        view.findViewById(R.id.profile_btn).setOnClickListener(this);
//        view.findViewById(R.id.meeting_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_btn:
                navController.navigate(R.id.action_fragmentMenu_to_fragmentProfile);
                break;
            case R.id.meeting_btn:
                navController.navigate(R.id.action_fragmentMenu_to_fragmentMeetingMenu);
                break;
        }
    }
}