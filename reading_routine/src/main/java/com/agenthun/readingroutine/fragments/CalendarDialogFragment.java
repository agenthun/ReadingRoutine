package com.agenthun.readingroutine.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.transitionmanagers.ITFragment;
import com.agenthun.readingroutine.transitionmanagers.TDialogFragment;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Henry on 2015/7/17.
 */
public class CalendarDialogFragment extends DialogFragment implements ITFragment {
    @InjectView(R.id.calendarView)
    MaterialCalendarView calendarView;
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    protected Object mDataIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calendar, container, false);
        ButterKnife.inject(this, view);

        Calendar calendar = Calendar.getInstance();
        calendarView.setSelectedDate(calendar.getTime());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.time_setting_finish_button)
    public void onTimeSettingFinishBtnClick() {
        Log.d("CalendarDialogFragment", "onTimeSettingFinishBtnClick");
    }

    @Override
    public void onEnter(Object data) {
        mDataIn = data;
    }

    @Override
    public void onLeave() {

    }

    @Override
    public void onBack() {

    }

    @Override
    public void onBackWithData(Object data) {

    }

    @Override
    public boolean processBackPressed() {
        return false;
    }
}
