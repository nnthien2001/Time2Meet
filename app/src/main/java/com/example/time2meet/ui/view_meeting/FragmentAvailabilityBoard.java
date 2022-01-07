package com.example.time2meet.ui.view_meeting;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.time2meet.R;
import com.example.time2meet.data.AvailabilityBoard;
import com.example.time2meet.data.MeetingViewModel;
import com.example.time2meet.data.UserViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
    private Boolean isEdit;
    private ArrayList<Integer> selected;
    private Map<Integer, Integer> availability;
    private View view;
    private int n_column;
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
        // Pls đừng gọi ViewModel ntn
        // sai lắm luôn á vì nó tạo new instance của VM ko phải cái chung
        // xem onViewCreated() trong FragmentHome
        // NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.meeting_nav_graph);
        // meetingViewModel = new ViewModelProvider(backStackEntry).get(MeetingViewModel.class);

        // oke bro. sorry =(((((( didn't mean to
        userViewModel = new UserViewModel();
        meetingViewModel = new MeetingViewModel();
        if (getArguments() != null) {
            meetingViewModel.goMeeting(getArguments().getInt("meetingID"));
        }
        availabilityBoard = new AvailabilityBoard(meetingViewModel.getMeeting());
        isEdit = false;
        this.view = view;
        selected = getCurrentSelection();
        n_column = (int) ((availabilityBoard.getEndDate().getTime() -
                availabilityBoard.getStartDate().getTime()) / 1000 / 24 / 3600) + 1;
        initBoard();
        drawCells();
        initAppBar();
    }

    private void initAppBar() {
        TextView tv_appbar = (TextView) view.findViewById(R.id.tv_action_bar_center);
        tv_appbar.setText(getResources().getString(R.string.availability_board));
        tv_appbar.setGravity(Gravity.START);
        tv_appbar.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.action_bar_text_size));
        ImageButton imgBtn_back = (ImageButton) view.findViewById(R.id.btn_action_bar_leftmost);
        imgBtn_back.setImageResource(R.drawable.ic_back);
        imgBtn_back.setColorFilter(Color.argb(255,255,255,255));
        ImageButton imgBtn_edit = (ImageButton) view.findViewById(R.id.btn_action_bar_rightmost);
        imgBtn_edit.setImageResource(R.drawable.ic_edit);
        imgBtn_edit.setColorFilter(Color.argb(255,255,255,255));
        imgBtn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIsEdit((ImageButton) view);
            }
        });
    }

    private void setIsEdit(ImageButton view) {
        isEdit = !isEdit;
        if (isEdit) {
            view.setImageResource(R.drawable.ic_save);
        }
        else {
            meetingViewModel.toggleTime(selected);
            availabilityBoard = new AvailabilityBoard(meetingViewModel.getMeeting());
            view.setImageResource(R.drawable.ic_edit);
        }
        drawCells();
    }

    public void initBoard() {
        board = (TableLayout) view.findViewById(R.id.availability_board);

        for (int i = 0; i < n_column; ++i) {
            // first row
            Date date = new Date(availabilityBoard.getStartDate().getTime() + 3600 * 24 * 1000 * i);
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
                ImageButton ib = new ImageButton(this.getContext());
                ib.setId(6000+j-1+i*24);
                ib.setLayoutParams(new TableRow.LayoutParams((int) (85 * this.getContext().getResources().getDisplayMetrics().density),
                        (int) (35 * this.getContext().getResources().getDisplayMetrics().density)));
                ib.setScaleType(ImageButton.ScaleType.CENTER);
                ib.setAdjustViewBounds(true);
                ib.setPadding(0, 0, 0, 0);

                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        select((ImageButton) view);
                    }
                });
                row.addView(ib);
            }
        }
    }

    private void drawCells() {
        Map<Integer, Integer> availability = availabilityBoard.getAvailability();

        for (int i = 0;i < n_column; ++i) {
            for (int j = 1; j < 25; ++j) {
                int view_id = j-1+i*24;
                ImageButton ib = view.findViewById(6000+view_id);

                if  (availability.get(j-1+i*24) == 0) {
                    ib.setBackgroundResource(R.drawable.cell_border);
                }
                else if (availability.get(j-1+i*24) < 3) {
                    ib.setBackground(this.getContext().getDrawable(R.drawable.cell_less_than_3));
                }
                else if (availability.get(j-1+i*24) < 6) {
                    ib.setBackground(this.getContext().getDrawable(R.drawable.cell_3_to_6));
                }
                else {
                    ib.setBackground(this.getContext().getDrawable(R.drawable.cell_greater_than_6));
                }

                if (isEdit && selected.contains(j-1+i*24)){
                    ib.setImageResource(R.drawable.cell_selected);
                }
                else {
                    ib.setImageResource(0);
                }
            }
        }
    }

    private void select(ImageButton view) {
        if (!isEdit) return;
        Integer id = view.getId();
        id = id - 6000;
        if (selected.contains(id)){
            view.setImageResource(0);
            selected.remove(id);
        }
        else {
            view.setImageResource(R.drawable.cell_selected);
            selected.add(id);
        }
    }

    private ArrayList<Integer> getCurrentSelection() {
        Map<Integer, ArrayList<Integer>> meeting_avai = meetingViewModel.getMeeting().getAttendees();
        Integer userID = userViewModel.getCurrentUser().getUserID();
        if (meeting_avai.get(userID)!= null)
            return meeting_avai.get(userViewModel.getCurrentUser().getUserID());
        return new ArrayList<>();
    }
}