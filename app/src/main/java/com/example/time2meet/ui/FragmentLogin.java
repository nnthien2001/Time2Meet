package com.example.time2meet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.time2meet.ApiService;
import com.example.time2meet.R;
import com.example.time2meet.data.Authenticator;
import com.example.time2meet.data.UserViewModel;
import com.example.time2meet.data.User;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLogin extends Fragment implements View.OnClickListener {

    private NavController navController;
    private UserViewModel userViewModel;
    private EditText edtUsername;
    private EditText edtPassword;

    public FragmentLogin() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph);
        userViewModel = new ViewModelProvider(backStackEntry).get(UserViewModel.class);

        typeinInfoSetup(view);
        buttonsSetup(view);
    }

    private void typeinInfoSetup(@NonNull View view) {
        edtUsername = (EditText) view.findViewById(R.id.edt_username_login);
        edtPassword = (EditText) view.findViewById(R.id.edt_password_login);
    }

    private void buttonsSetup(@NonNull View view) {
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.btn_signup).setOnClickListener(this);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (userViewModel.login(edtUsername.getText().toString()
                        , edtPassword.getText().toString())) {
                        navController.navigate(R.id.action_fragmentLogin_to_fragmentHome);
                }
                else {
                        // TODO: Handle error message
                        break;
                }
            case R.id.btn_signup:
                // Check username exist: userViewModel.isUsernameExist(username)
                // TODO: action move from FragmentLogin to FragmentSignUp
                break;
        }
    }
}