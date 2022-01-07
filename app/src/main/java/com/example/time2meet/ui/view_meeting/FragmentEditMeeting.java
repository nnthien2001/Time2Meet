package com.example.time2meet.ui.view_meeting;

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
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time2meet.R;
import com.example.time2meet.data.DatePickerHelper;
import com.example.time2meet.data.Helper;
import com.example.time2meet.data.Meeting;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.databinding.FragmentEditMeetingBinding;
import com.example.time2meet.databinding.PopupConfirmActionBinding;

import java.util.Calendar;
import java.util.Locale;

public class FragmentEditMeeting extends Fragment {

    private FragmentEditMeetingBinding binding;
    private NavController navController;
    private MeetingViewModel meetingViewModel;
    private Meeting meeting;

    public FragmentEditMeeting() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);

        // set up meeting
        meeting = meetingViewModel.getMeeting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditMeetingBinding.inflate(inflater, container, false);
        //return inflater.inflate(R.layout.fragment_edit_meeting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        navController = Navigation.findNavController(view);
//
//        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
//        meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);

        // set up binding view
        binding.meetingEditName.setText(meeting.getMeetingName());
        if (meeting.getDate() != null && !meeting.getDate().equals("")) {
            binding.meetingEditTime.setText(meeting.getDate());
        }
        DatePickerHelper datePickerHelper = new DatePickerHelper(getContext(), view);
        datePickerHelper.datePickerSetup(binding.meetingEditTime);
        binding.meetingEditLocation.setText(meeting.getLocation());
        binding.meetingEditDescription.setText(meeting.getDescription());

        binding.meetingBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
            }
        });
        binding.meetingBtnDeleteMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmDelete(v);
            }
        });

        setupAppBar();
    }

    private void setupAppBar(){
        TextView appbar_title = getView().findViewById(R.id.tv_action_bar_center);
        appbar_title.setText(getResources().getString(R.string.edit_meeting));
        appbar_title.setGravity(Gravity.START);
        appbar_title.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.action_bar_text_size));

        ImageButton back_button =getView().findViewById(R.id.btn_action_bar_leftmost);
        ImageButton save_button =getView().findViewById(R.id.btn_action_bar_rightmost);
        back_button.setImageResource(R.drawable.ic_back);
        save_button.setImageResource(R.drawable.ic_save);
        back_button.setColorFilter(Color.argb(255,255,255,255));
        save_button.setColorFilter(Color.argb(255,255,255,255));

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEdit();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigateUp();
            }
        });
    }

    void openConfirmDelete(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.popup_confirm_action, null);
        PopupWindow popupWindow = new PopupWindow(layout, (int) (300 * this.getContext().getResources().getDisplayMetrics().density),
                (int) (200 * this.getContext().getResources().getDisplayMetrics().density),
                true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        
        PopupConfirmActionBinding popupBinding = PopupConfirmActionBinding.bind(layout);
        popupBinding.confirmStatement.setText(R.string.confirm_delete_stm);
        popupBinding.confirmStatement.setGravity(Gravity.CENTER);
        popupBinding.confirmStatement.setAllCaps(false);

        popupBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupBinding.btnConfirm.setText(R.string.delete);
        popupBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                deleteMeeting();
            }
        });
    }

    private void deleteMeeting() {
        meetingViewModel.deleteMeeting();

        navController.popBackStack(R.id.fragmentHome, false);
    }

    private void saveEdit() {
        String name = binding.meetingEditName.getText().toString();
        String time = binding.meetingEditTime.getText().toString();
        String location = binding.meetingEditLocation.getText().toString();
        String description = binding.meetingEditDescription.getText().toString();
        Helper helper = Helper.getInstance();
        if (time.equals("Undecided") || (helper.compareDate(time, meetingViewModel.getMeeting().getStartDate()) >= 0 &&
            helper.compareDate(time, meetingViewModel.getMeeting().getEndDate()) <= 0)) {
            meetingViewModel.updateMeeting(name, time, location, description);
            navController.navigateUp();
        }
        else {
            Toast.makeText(getContext(), "The official date is out of available date range", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}