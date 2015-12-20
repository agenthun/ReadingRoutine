package com.agenthun.readingroutine.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.agenthun.readingroutine.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Henry on 2015/7/17.
 */
public class CalendarDialogFragment extends AppCompatDialogFragment {
    private static final String TAG = "CalendarDialogFragment";

    public static final String SELECTD_DATE = "SELECTD_DATE";

    private CalendarCallback mCalendarCallback;

    public CalendarDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_calendar, null);
        final MaterialCalendarView calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        final Date mDate = new Date();
        mDate.setTime(getArguments().getLong(SELECTD_DATE));
        calendarView.setSelectedDate(mDate);

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCalendarCallback != null) {
                            Date selectedDate = Calendar.getInstance().getTime();
                            selectedDate.setYear(calendarView.getSelectedDate().getDate().getYear());
                            selectedDate.setMonth(calendarView.getSelectedDate().getDate().getMonth());
                            selectedDate.setDate(calendarView.getSelectedDate().getDate().getDate());
                            mCalendarCallback.onDateTimeSet(selectedDate);
                        }
                    }
                })
                .setNegativeButton(R.string.text_cancel, null)
                .create();
    }

    public void setCalendarCallback(CalendarCallback mCalendarCallback) {
        this.mCalendarCallback = mCalendarCallback;
    }

    public interface CalendarCallback {
        void onDateTimeSet(Date date);
    }
}
