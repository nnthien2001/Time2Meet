package com.example.time2meet.ui.login;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.time2meet.R;
import com.example.time2meet.data.Helper;
import com.example.time2meet.data.UserViewModel;

public class FragmentLogin extends Fragment {

    private NavController navController;
    private UserViewModel userViewModel;

    private EditText edtUsername;
    private EditText edtPassword;
    private boolean hidePassword = true;

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
            if (edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals(""))
                return;
            if (userViewModel.login(edtUsername.getText().toString(),
                    Helper.hashPassword(edtPassword.getText().toString()))) {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentHome);
            }
            else {
                Toast.makeText(
                        this.getContext(),
                        "Username and password do not match or this account does not exist.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        view.findViewById(R.id.btn_to_signup).setOnClickListener(v -> {
            navController.navigate(R.id.action_fragmentLogin_to_fragmentSignup);
        });

        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    int unhideBtnBound = edtPassword.getRight() - edtPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - edtPassword.getCompoundPaddingRight();
                    if(event.getRawX() >= unhideBtnBound) {
                        if (hidePassword) {
                            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_key_pass, 0, R.drawable.ic_password_hide, 0);
                        }
                        else {
                            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_key_pass, 0, R.drawable.ic_password_unhide, 0);
                        }
                        hidePassword = !hidePassword;
                        return true;
                    }
                }
                return false;
            }
        });
    }

}