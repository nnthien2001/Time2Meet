package com.example.time2meet.ui.login;

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

import com.example.time2meet.R;
import com.example.time2meet.data.Helper;
import com.example.time2meet.data.UserViewModel;

public class FragmentLogin extends Fragment {

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
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph); //for debug
        userViewModel = new ViewModelProvider(backStackEntry).get(UserViewModel.class);

        typeinInfoSetup(view);
        buttonsSetup(view);
    }

    private void typeinInfoSetup(@NonNull View view) {
        edtUsername = view.findViewById(R.id.edt_username_login);
        edtPassword = view.findViewById(R.id.edt_password_login);
    }

    private void buttonsSetup(@NonNull View view) {
        view.findViewById(R.id.btn_login).setOnClickListener(v -> {
            if (userViewModel.login(edtUsername.getText().toString(),
                    Helper.hashPassword(edtPassword.getText().toString()))) {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentHome);
            }
            else {
                // TODO: Handle error message
            }
        });

        view.findViewById(R.id.btn_to_signup).setOnClickListener(v -> {
            // Check username exist: userViewModel.isUsernameExist(username)
            // TODO: action move from FragmentLogin to FragmentSignUp
            navController.navigate(R.id.action_fragmentLogin_to_fragmentSignup);
        });
    }

}