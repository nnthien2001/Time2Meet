package com.example.time2meet.ui;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.User;
import com.example.time2meet.data.UserViewModel;

public class FragmentProfile extends Fragment {
    private UserViewModel userViewModel;
    private NavController navController;
    private User current_user;
    private TextView username;
    private TextView desc_fullname;
    private TextView fullname;
    private TextView desc_dob;
    private TextView dob,desc_phone,phone,desc_aboutme,aboutme;
    private Button edit_password_button;
    private ImageButton back_button;
    private ImageButton edit_button;
    private CircularImageView user_avatar;
    private Integer userID = null;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        userViewModel = new UserViewModel();

        // Get the userID from view attendance list fragment
        if(getArguments()!=null) {
            current_user = userViewModel.getUser(getArguments().getInt("userID"));
            userID = getArguments().getInt("userID");
        }
        else {
            current_user = userViewModel.getCurrentUser();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    private void initAppBar(){
        TextView appbar_title = getView().findViewById(R.id.tv_action_bar_center);
        appbar_title.setText(getResources().getString(R.string.my_profile));
        appbar_title.setGravity(Gravity.START);
        appbar_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.action_bar_text_size));
        Log.d("Title textSize", String.valueOf(appbar_title.getTextSize()));
        back_button =getView().findViewById(R.id.btn_action_bar_leftmost);
        edit_button =getView().findViewById(R.id.btn_action_bar_rightmost);
        back_button.setImageResource(R.drawable.ic_back);
        edit_button.setImageResource(R.drawable.ic_edit);
        back_button.setColorFilter(Color.argb(255,255,255,255));
        edit_button.setColorFilter(Color.argb(255,255,255,255));
        //FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add backstack when navigating?
                navController.navigate(R.id.action_fragmentProfile_to_fragmentEditProfile);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Pop the backstack after finish viewing?
                navController.navigateUp();
            }
        });
        if(isDifferentUserView()) {
            edit_button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAppBar();

        initialize_elements();
        setUserInfo();
    }

    public void setUserInfo(){
        username.setText(current_user.getUsername());
        fullname.setText(current_user.getName());
        dob.setText(current_user.getDob());
        phone.setText(current_user.getPhone());
        aboutme.setText(current_user.getAbout());
        desc_fullname.setText(getResources().getString(R.string.fullname));
        desc_dob.setText(getResources().getString(R.string.dob));
        desc_phone.setText(getResources().getString(R.string.phone_num));
        desc_aboutme.setText(getResources().getString(R.string.about_me));
        String avatar_id=current_user.getImage();
        int avatar_src= getResources().getIdentifier(avatar_id,"drawable",getContext().getPackageName());
        user_avatar.setImageResource(avatar_src);
    }
    public void initialize_elements(){
        username=getView().findViewById(R.id.profile_username);
        dob=getView().findViewById(R.id.profile_dob);
        aboutme=getView().findViewById(R.id.profile_about_me);
        phone=getView().findViewById(R.id.profile_phone_num);
        fullname=getView().findViewById(R.id.profile_fullname);

        desc_fullname= getView().findViewById(R.id.profile_desc_fullname);
        desc_dob=getView().findViewById(R.id.profile_desc_dob);
        desc_aboutme=getView().findViewById(R.id.profile_desc_about_me);
        desc_phone=getView().findViewById(R.id.profile_desc_phone_num);

        user_avatar=getView().findViewById(R.id.user_avatar);
        edit_password_button=getView().findViewById(R.id.button_edit_password);
        edit_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_fragmentProfile_to_fragmentEditPassword);
            }
        });
        if(isDifferentUserView()) {
            edit_password_button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isDifferentUserView()) {
            current_user = userViewModel.getUser(userID);
        }
        else {
            current_user=userViewModel.getCurrentUser();
        }
        setUserInfo();
    }

    private boolean isDifferentUserView() {
        return userID != null && !userID.equals(userViewModel.getCurrentUser().getUserID());
    }
}