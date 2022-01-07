package com.example.time2meet.ui.profile;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.DatePickerHelper;
import com.example.time2meet.data.User;
import com.example.time2meet.data.UserViewModel;

import java.util.Calendar;
import java.util.Locale;


public class FragmentEditProfile extends Fragment {
    private UserViewModel userViewModel;
    private NavController navController;
    private User current_user;
    private EditText username, fullname, dob, phone, aboutme;
    private TextView desc_fullname, desc_phone, desc_dob, desc_aboutme;

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

    private void initAppBar() {
        TextView appbar_title = (TextView) getView().findViewById(R.id.tv_action_bar_center);
        appbar_title.setText(getResources().getString(R.string.my_profile));
        appbar_title.setGravity(Gravity.START);
        appbar_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.action_bar_text_size));
        ImageButton back_button = (ImageButton) getView().findViewById(R.id.btn_action_bar_leftmost);
        ImageButton save_button = (ImageButton) getView().findViewById(R.id.btn_action_bar_rightmost);
        back_button.setImageResource(R.drawable.ic_back);
        save_button.setImageResource(R.drawable.ic_save);
        back_button.setColorFilter(Color.argb(255,255,255,255));
        save_button.setColorFilter(Color.argb(255,255,255,255));
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEdited(view);
                navController.navigateUp();
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
        navController = Navigation.findNavController(view);

        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph);
        userViewModel = new ViewModelProvider(backStackEntry).get(UserViewModel.class);
        current_user = userViewModel.getCurrentUser();

        initialize_elements();
        setUserInfo();
        DatePickerHelper datePickerHelper = new DatePickerHelper(getContext(), view);
        datePickerHelper.datePickerSetup((TextView) view.findViewById(R.id.edit_profile_dob));
    }

    public void setUserInfo() {
        username.setText(current_user.getUsername());
        dob.setText(current_user.getDob());
        phone.setText(current_user.getPhone());
        aboutme.setText(current_user.getAbout());
        fullname.setText(current_user.getName());
        desc_fullname.setText(getResources().getString(R.string.fullname));
        desc_dob.setText(getResources().getString(R.string.dob));
        desc_phone.setText(getResources().getString(R.string.phone_num));
        desc_aboutme.setText(getResources().getString(R.string.about_me));
        ((CircularImageView) getView().findViewById(R.id.edit_user_avatar))
                .setImageResource(getResources().getIdentifier(current_user.getImage(),
                        "drawable",getContext().getPackageName()));
    }

    private void setEdited(View view) {
//        current_user.setUsername(username.getText().toString());
//        current_user.setAbout(aboutme.getText().toString());
//        current_user.setDob(dob.getText().toString());
//        current_user.setName(fullname.getText().toString());
//        current_user.setPhone(phone.getText().toString());
        userViewModel.updateProfile(username.getText().toString(),
                fullname.getText().toString(),
                dob.getText().toString(),
                phone.getText().toString(),
                aboutme.getText().toString());
        current_user = userViewModel.getCurrentUser();
        Log.d("Username",current_user.getUsername());
        Log.d("Name",current_user.getName());

    }

//    private void dobPickerSetup(Context context, View view) {
//        TextView tv_dob = (TextView) view.findViewById(R.id.edit_profile_dob);
//
//        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                //String date = day + "/" + month + "/" + year;
//                String date = String.format(Locale.ENGLISH,"%02d/%02d/%04d", day, month, year);
//                tv_dob.setText(date);
//            }
//        };
//
//        tv_dob.setInputType(InputType.TYPE_NULL);
//        tv_dob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePicker(context, onDateSetListener);
//            }
//        });
//        tv_dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    showDatePicker(context, onDateSetListener);
//                }
//            }
//        });
//    }
//
//    private void showDatePicker(Context context, DatePickerDialog.OnDateSetListener onDateSetListener) {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog dialog = new DatePickerDialog(
//                context,
//                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                onDateSetListener,
//                year, month, day);
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//    }

    public void initialize_elements() {
        username = (EditText) getView().findViewById(R.id.edit_profile_username);
        fullname = (EditText) getView().findViewById(R.id.edit_profile_fullname);
        dob = (EditText) getView().findViewById(R.id.edit_profile_dob);
        aboutme = (EditText) getView().findViewById(R.id.edit_profile_about_me);
        phone = (EditText) getView().findViewById(R.id.edit_profile_phone_num);

        desc_fullname = (TextView) getView().findViewById(R.id.edit_profile_desc_fullname);
        desc_dob = (TextView) getView().findViewById(R.id.edit_profile_desc_dob);
        desc_aboutme = (TextView) getView().findViewById(R.id.edit_profile_desc_about_me);
        desc_phone = (TextView) getView().findViewById(R.id.edit_profile_desc_phone_num);
    }

    @Override
    public void onResume() {
        super.onResume();
        current_user=userViewModel.getCurrentUser();
        setUserInfo();
    }
}