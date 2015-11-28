package com.agenthun.readingroutine.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.readingroutine.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Henry on 2015/7/17.
 */
public class CalendarDialogFragment extends AppCompatDialogFragment {
    @InjectView(R.id.calendarView)
    MaterialCalendarView calendarView;
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Date mDate;

    private CalendarCallback mCalendarCallback;

    public CalendarDialogFragment() {

    }

    public CalendarDialogFragment(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calendar, container, false);
        ButterKnife.inject(this, view);

        calendarView.setSelectedDate(mDate);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

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
