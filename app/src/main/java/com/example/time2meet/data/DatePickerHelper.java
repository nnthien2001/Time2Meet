package com.example.time2meet.data;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.time2meet.R;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerHelper {
    private Context context;
    private View view;

    public DatePickerHelper(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void datePickerSetup(TextView tv) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //String date = day + "/" + month + "/" + year;
                String date = String.format(Locale.ENGLISH,"%02d/%02d/%04d", day, month, year);
                tv.setText(date);
            }
        };

        tv.setInputType(InputType.TYPE_NULL);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(context, onDateSetListener);
            }
        });
        tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(context, onDateSetListener);
                }
            }
        });
    }

    private void showDatePicker(Context context, DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,
                year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
