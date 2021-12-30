package com.example.time2meet.ui;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.User;
import com.example.time2meet.data.UserViewModel;

import java.util.Calendar;


public class FragmentEditProfile extends Fragment {
    private UserViewModel userViewModel;
    private NavController navController;
    private User current_user;
    private EditText username,full_name,dob,phone,about_me;
    private TextView desc_fullname,desc_phone,desc_dob,desc_about_me;

    public FragmentEditProfile() {
        // Required empty public constructor
    }
    
    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }
    private void initAppBar(){
        TextView appbar_title = (TextView) getView().findViewById(R.id.tv_action_bar_center);
        appbar_title.setText(getResources().getString(R.string.my_profile));
        ImageButton back_button = (ImageButton) getView().findViewById(R.id.btn_action_bar_leftmost);
        ImageButton save_button =(ImageButton) getView().findViewById(R.id.btn_action_bar_rightmost);
        back_button.setImageResource(R.drawable.ic_back);
        save_button.setImageResource(R.drawable.ic_save);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEdited(view);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add back functionality
                navController.navigateUp();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAppBar();
        navController= Navigation.findNavController(view);

        NavBackStackEntry backStackEntry=navController.getBackStackEntry(R.id.nav_graph);
        userViewModel=new ViewModelProvider(backStackEntry).get(UserViewModel.class);
        current_user=userViewModel.getCurrentUser();

        setUserInfo();
        dobPickerSetup(getContext(),view);
    }
    public void setUserInfo(){
        username.setText(current_user.getUsername());
        dob.setText(current_user.getDob());
        phone.setText(current_user.getPhone());
        about_me.setText(current_user.getAbout());
        desc_fullname.setText(getResources().getString(R.string.fullname));
        desc_dob.setText(getResources().getString(R.string.dob));
        desc_phone.setText(getResources().getString(R.string.phone_num));
        desc_about_me.setText(getResources().getString(R.string.about_me));
    }

    private void setEdited(View view) {
        current_user.setUsername(username.getText().toString());
        current_user.setAbout(about_me.getText().toString());
        current_user.setDob(dob.getText().toString());
        current_user.setName(full_name.getText().toString());
        current_user.setPhone(phone.getText().toString());
        userViewModel.updateProfile(current_user.getUsername(),
                current_user.getName(),
                current_user.getDob(),
                current_user.getPhone(),
                current_user.getAbout());
    }
    private void dobPickerSetup(Context context, View view) {
        TextView tv_dob = (TextView) view.findViewById(R.id.profile_dob);

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