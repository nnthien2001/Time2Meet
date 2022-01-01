package com.example.time2meet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.time2meet.R;
import com.example.time2meet.data.UserViewModel;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class FragmentLanding extends Fragment {

    private NavController navController;
    private UserViewModel userViewModel;
    CountDownTimer timer = null;

    public FragmentLanding() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph); //for debug

        timer = new CountDownTimer(1000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                navController.navigate(R.id.action_fragmentLanding_to_fragmentLogin);
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(timer != null)
            timer.cancel();
    }
}