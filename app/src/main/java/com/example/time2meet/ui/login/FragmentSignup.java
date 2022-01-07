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

import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time2meet.R;
import com.example.time2meet.data.Helper;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.data.UserRepository;

import java.util.Calendar;
import java.util.Locale;

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
        btn_leftmost.setOnClickListener(v -> navController.navigateUp());

        tv_center.setText(getResources().getString(R.string.sign_up));
        tv_center.setGravity(Gravity.LEFT);
        tv_center.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.action_bar_text_size));

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
            //String date = day + "/" + month + "/" + year;
            String date = String.format(Locale.ENGLISH,"%02d/%02d/%04d", day, month, year);
            tv_dob.setText(date);
        };

        tv_dob.setInputType(InputType.TYPE_NULL);
        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(context, onDateSetListener);
            }
        });
        tv_dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(context, onDateSetListener);
                }
            }
        });
    }

    private void showDatePicker(Context context, DatePickerDialog.OnDateSetListener onDateSetListener) {
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

    private void buttonsSetup(View view) {
        view.findViewById(R.id.btn_create_account).setOnClickListener(v -> {
            if (edt_username.getText().toString().equals("")
                || edt_password.getText().toString().equals("")
                || edt_confirm_password.getText().toString().equals(""))
                return;

            if (userViewModel.isUsernameExist(edt_username.getText().toString())) {
                Log.d("Create account", "Username already exists.");

                Toast.makeText(
                        this.getContext(),
                        "Username already exists.",
                        Toast.LENGTH_LONG
                ).show();
            }
            else if (edt_password.getText().toString().equals(edt_confirm_password.getText().toString())) {
                int rqstate = userViewModel.signUp(
                        edt_username.getText().toString(),
                        Helper.hashPassword(edt_password.getText().toString()),
                        edt_name.getText().toString(),
                        edt_dob.getText().toString(),
                        edt_phone_number.getText().toString());

                if (rqstate == UserRepository.getInstance().REQUEST_SUCCESS) {
                    Log.d("Create account", "Sign up successfully!");

                    rqstate = userViewModel.login(
                            edt_username.getText().toString(),
                            Helper.hashPassword(edt_password.getText().toString())
                    ) ? 1 : 0;

                    if (rqstate == 1) {
                        Log.d("Create account", "Login successfully!");
                    }
                    else {
                        Log.d("Create account button", "Login failed!");

                        Toast.makeText(
                                this.getContext(),
                                "Something went wrong. Login failed.",
                                Toast.LENGTH_LONG
                        ).show();

                        navController.navigateUp();
                    }

                    navController.navigate(R.id.action_fragmentSignup_to_fragmentHome);
                }
                else {
                    Log.d("Create account", "Sign up failed!");

                    Toast.makeText(
                            this.getContext(),
                            "Something went wrong. Sign up failed.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
            else {
                Log.d("Create account", "Passwords mismatch.");

                Toast.makeText(
                        this.getContext(),
                        "Please make sure your passwords match.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        view.findViewById(R.id.btn_to_login).setOnClickListener(
                v -> navController.navigateUp());
    }

}