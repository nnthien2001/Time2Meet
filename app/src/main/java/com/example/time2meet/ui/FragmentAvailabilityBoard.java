package com.example.time2meet.ui;


import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.AvailabilityBoard;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.UserViewModel;

import java.util.Date;

/**
 * A simple @link Fragment subclass.
 * Use the @link FragmentAvailabilityBoard#newInstance factory method to
 * create an instance of this fragment.
 **/
public class FragmentAvailabilityBoard extends Fragment {
    private MeetingViewModel meetingViewModel;
    private UserViewModel userViewModel;
    private AvailabilityBoard availabilityBoard;
    private TableLayout board;

    public FragmentAvailabilityBoard() {
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
        return inflater.inflate(R.layout.fragment_availability_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new UserViewModel();
        meetingViewModel = new MeetingViewModel();
        meetingViewModel.goMeeting(2);
        availabilityBoard = new AvailabilityBoard(meetingViewModel.getMeeting());
        board = (TableLayout) view.findViewById(R.id.availability_board);
        initBoard(view);
    }

    public void initBoard(View view) {
        int n_col = (int) ((availabilityBoard.getEndDate().getTime() -
                availabilityBoard.getStartDate().getTime()) / 1000 / 24 / 3600) + 1;
        for (int i = 0; i < n_col; ++i) {
            // first row
            Date date = new Date(availabilityBoard.getStartDate().getTime() + 3600 * 24 * 1000 * i);
            Log.i("debug", availabilityBoard.toDateLabel(date));
            TableRow time_label_row = (TableRow) view.findViewById(R.id.time_label);
            TextView time_label_txt = new TextView(this.getContext());
            time_label_txt.setWidth((int) (85 * this.getContext().getResources().getDisplayMetrics().density));
            time_label_txt.setHeight((int) (35 * this.getContext().getResources().getDisplayMetrics().density));
            time_label_txt.setGravity(Gravity.CENTER);
            time_label_txt.setBackground(this.getContext().getDrawable(R.drawable.cell_border));
            time_label_txt.setText(availabilityBoard.toDateLabel(date));
            time_label_row.addView(time_label_txt);

            // the rest
            for (int j = 1;j < 25; ++j) {
                TableRow row = (TableRow) board.getChildAt(j);
                TextView tv = new TextView(this.getContext());
                tv.setBackground(this.getContext().getDrawable(R.drawable.cell_border));
                tv.setId(6000+j-1+i*24);
                tv.setWidth((int) (85 * this.getContext().getResources().getDisplayMetrics().density));
                tv.setHeight((int) (35 * this.getContext().getResources().getDisplayMetrics().density));
                tv.setGravity(Gravity.CENTER);
                row.addView(tv);
            }
        }
    }
}