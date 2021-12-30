package com.example.time2meet.ui.login;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.UserViewModel;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSignup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSignup extends Fragment {

    private NavController navController;
    private UserViewModel userViewModel;

    public FragmentSignup() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph); //for debug
        userViewModel = new ViewModelProvider(backStackEntry).get(UserViewModel.class);

        actionBarSetup(view);
        dobPickerSetup(getContext(), view);
    }

    private void actionBarSetup(View view) {
        ImageButton btn_leftmost = (ImageButton) view.findViewById(R.id.btn_action_bar_leftmost);
        TextView tv_center = (TextView) view.findViewById(R.id.tv_action_bar_center);
        ImageButton btn_rightmost = (ImageButton) view.findViewById(R.id.btn_action_bar_rightmost);

        btn_leftmost.setImageResource(R.drawable.back_icon);

        tv_center.setText("Sign up");
        tv_center.setGravity(Gravity.LEFT);

        btn_rightmost.setImageDrawable(null);
    }

    private void dobPickerSetup(Context context, View view) {
        TextView tv_dob = (TextView) view.findViewById(R.id.edt_dob_signup);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                tv_dob.setText(date);
            }
        };

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

}