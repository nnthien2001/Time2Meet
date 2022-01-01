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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time2meet.R;
import com.example.time2meet.data.Helper;
import com.example.time2meet.data.User;
import com.example.time2meet.data.UserViewModel;

public class FragmentEditPassword extends Fragment {
    private NavController navController;
    private User current_user;
    private UserViewModel userViewModel;
    private EditText current_password;
    private EditText new_password;
    private EditText confirm_password;
    //private Helper

    public FragmentEditPassword() {
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
        return inflater.inflate(R.layout.fragment_edit_password, container, false);
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
    }

    private void initAppBar() {
        TextView appbar_title = (TextView) getView().findViewById(R.id.tv_action_bar_center);
        appbar_title.setText(getResources().getString(R.string.edit_password));
        ImageButton back_button = (ImageButton) getView().findViewById(R.id.btn_action_bar_leftmost);
        ImageButton save_button = (ImageButton) getView().findViewById(R.id.btn_action_bar_rightmost);
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
    private void initialize_elements(){
        current_password=(EditText) getView().findViewById(R.id.edit_current_pass);
        new_password=(EditText) getView().findViewById(R.id.edit_new_password);
        confirm_password=(EditText) getView().findViewById(R.id.edit_confirm_password);
    }

    private void setEdited(View view) {
        //TODO: Hash password and update
        String current=current_password.getText().toString();
        String new_pass=new_password.getText().toString();
        String confirm=confirm_password.getText().toString();
        //Hash and check all three before saving
        if (current==current_user.getPassword() && new_pass==confirm)
        {
            //Update password
            userViewModel.changePassword(new_pass);
            Toast.makeText(getContext(),"Password updated!",Toast.LENGTH_LONG).show();
            navController.navigateUp();
        }
        else Toast.makeText(getContext(),"Incorrect password or new passwords are different",Toast.LENGTH_LONG).show();

    }
}