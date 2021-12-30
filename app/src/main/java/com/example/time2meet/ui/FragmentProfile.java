package com.example.time2meet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

    public FragmentProfile() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    private void initAppBar(){
        TextView appbar_title = (TextView) getView().findViewById(R.id.tv_action_bar_center);
        appbar_title.setText(getResources().getString(R.string.my_profile));
        back_button = (ImageButton) getView().findViewById(R.id.btn_action_bar_leftmost);
        edit_button =(ImageButton) getView().findViewById(R.id.btn_action_bar_rightmost);
        back_button.setImageResource(R.drawable.ic_back);
        edit_button.setImageResource(R.drawable.ic_edit);
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
    }

    public void setUserInfo(){
        username.setText(current_user.getUsername());
        dob.setText(current_user.getDob());
        phone.setText(current_user.getPhone());
        aboutme.setText(current_user.getAbout());
        desc_fullname.setText(getResources().getString(R.string.fullname));
        desc_dob.setText(getResources().getString(R.string.dob));
        desc_phone.setText(getResources().getString(R.string.phone_num));
        desc_aboutme.setText(getResources().getString(R.string.about_me));
    }
}