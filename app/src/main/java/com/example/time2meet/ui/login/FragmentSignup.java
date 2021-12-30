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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.Helper;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.data.UserRepository;

import java.util.Calendar;

public class FragmentSignup extends Fragment {

    private NavController navController;
    private UserViewModel userViewModel;

    private EditText edt_username;
    private EditText edt_password;
    private EditText edt_confirm_password;
    private EditText edt_name;
    private TextView edt_dob;
    private EditText edt_phone_number;

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
        typeinInfoSetup(view);
        dobPickerSetup(getContext(), view);
        buttonsSetup(view);
    }

    private void actionBarSetup(View view) {
        ImageButton btn_leftmost = view.findViewById(R.id.btn_action_bar_leftmost);
        TextView tv_center = view.findViewById(R.id.tv_action_bar_center);
        ImageButton btn_rightmost = view.findViewById(R.id.btn_action_bar_rightmost);

        btn_leftmost.setImageResource(R.drawable.back_icon);
        btn_leftmost.setOnClickListener(v -> navController.navigate(R.id.action_fragmentSignup_to_fragmentLogin));

        tv_center.setText("Sign up");
        tv_center.setGravity(Gravity.LEFT);

        btn_rightmost.setImageDrawable(null);
    }

    private void typeinInfoSetup(View view) {
        edt_username = view.findViewById(R.id.edt_username_signup);
        edt_password = view.findViewById(R.id.edt_password_signup);
        edt_confirm_password = view.findViewById(R.id.edt_confirm_password_signup);
        edt_name = view.findViewById(R.id.edt_name_signup);
        edt_dob = view.findViewById(R.id.edt_dob_signup);
        edt_phone_number = view.findViewById(R.id.edt_phone_number_signup);
    }

    private void dobPickerSetup(Context context, View view) {
        TextView tv_dob = view.findViewById(R.id.edt_dob_signup);

        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = day + "/" + month + "/" + year;
            tv_dob.setText(date);
        };

        tv_dob.setOnClickListener(v -> {
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
        });
    }

    private void buttonsSetup(View view) {
        view.findViewById(R.id.btn_create_account).setOnClickListener(v -> {
            if (edt_password.getText().toString().equals(edt_confirm_password.getText().toString())) {
                int rqstate = userViewModel.signUp(edt_username.getText().toString(),
                        Helper.hashPassword(edt_password.getText().toString()));
                if (rqstate == UserRepository.getInstance().REQUEST_SUCCESS) {
                    Log.d("Create account button set up", "Sign up successfully!");

                    rqstate = userViewModel.updateProfile(
                            edt_username.getText().toString(),
                            edt_name.getText().toString(),
                            edt_dob.getText().toString(),
                            edt_phone_number.getText().toString(),
                            ""
                    );

                    if (rqstate == UserRepository.getInstance().REQUEST_SUCCESS) {
                        Log.d("Create account button set up", "Update profile of new account successfully!");
                    }
                    else {
                        Log.d("Create account button set up", "Update profile of new account failed!");
                    }

                    // TODO: Handle error mesage for updating profile
                }
                else {
                    Log.d("Create account button set up", "Sign up failed!");

                    // TODO: Handle error message for signing up
                }
            }
            else {
                // TODO: Handle error message for confirming password
            }
        });

        view.findViewById(R.id.btn_to_login).setOnClickListener(
                v -> navController.navigate(R.id.action_fragmentSignup_to_fragmentLogin));
    }

}