package com.agenthun.readingroutine.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.readingroutine.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Henry on 2015/7/17.
 */
public class CalendarDialogFragment extends AppCompatDialogFragment {
    private static final String TAG = "CalendarDialogFragment";

    public static final String SELECTD_DATE = "SELECTD_DATE";
    @InjectView(R.id.calendarView)
    MaterialCalendarView calendarView;

    private CalendarCallback mCalendarCallback;

    public CalendarDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calendar, container, false);
        ButterKnife.inject(this, view);

        Date mDate = new Date();
        mDate.setTime(getArguments().getLong(SELECTD_DATE));
        calendarView.setSelectedDate(mDate);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
/*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_calendar, null);
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCalendarCallback != null) {
                            mCalendarCallback.onDateTimeSet(calendarView.getSelectedDate().getDate());
                        }
                    }
                })
                .setNegativeButton(R.string.text_cancel, null)
                .create();
    }*/

    @OnClick(R.id.time_setting_finish_button)
    public void onTimeSettingFinishBtnClick() {
        if (mCalendarCallback != null) {
            mCalendarCallback.onDateTimeSet(calendarView.getSelectedDate().getDate());
        }
        dismiss();
    }

    public void setCalendarCallback(CalendarCallback mCalendarCallback) {
        this.mCalendarCallback = mCalendarCallback;
    }

    public interface CalendarCallback {
        void onDateTimeSet(Date date);
    }
}
