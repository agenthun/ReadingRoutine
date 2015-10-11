package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.RoutinesAdapter;
import com.agenthun.readingroutine.fragments.CalendarDialogFragment;
import com.agenthun.readingroutine.fragments.MenuFragment;
import com.agenthun.readingroutine.fragments.RoutinesFragment;
import com.agenthun.readingroutine.fragments.SettingsFragment;
import com.agenthun.readingroutine.transitionmanagers.FragmentParam;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.agenthun.readingroutine.transitionmanagers.TFragment;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BookActivity extends TActivity {

    private static final String TAG = "BookActivity";
    private static final String SELECT_BOOK_ALARM_TIME = "SELECT_BOOK_ALARM_TIME";

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    @InjectView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.book_name_edittext)
    EditText bookName;
    @InjectView(R.id.alarm_time)
    TextView alarmTime;
    private String getBookAlarmTime;
    private Intent intent;
    private Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.inject(this);

        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setState(MaterialMenuDrawable.IconState.X);
        materialMenuIconToolbar.setNeverDrawTouch(false);

/*        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        toolbar.setTitle(R.string.text_book);*/
        //collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.color_white));
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.color_white));

        intent = getIntent();

        int[] colorBook = getResources().getIntArray(R.array.style_book_color);
        int getBookColorIndex = intent.getIntExtra(RoutinesAdapter.BOOK_COLOR_INDEX, new Random().nextInt(4));
        toolbar.setBackgroundColor(colorBook[getBookColorIndex]);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenuIconToolbar.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
                finish();
            }
        });

        String getBookName = intent.getStringExtra(RoutinesAdapter.BOOK_NAME);
        collapsingToolbarLayout.setTitle(getBookName);

        if (getBookName.compareTo(getResources().getString(R.string.text_book)) != 0) {
            bookName.setText(getBookName);
        }

        getBookAlarmTime = intent.getStringExtra(RoutinesAdapter.BOOK_ALARM_TIME);
        alarmTime.setText(getBookAlarmTime);
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.alarm_time)
    public void onAlarmTimeClick() {
        try {
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
            mDate = DATE_FORMAT.parse(getBookAlarmTime);
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            mDate = calendar.getTime();
        }
        CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment(mDate);
        calendarDialogFragment.show(getSupportFragmentManager(), TAG);
        calendarDialogFragment.setCalendarCallback(mCalendarFragmentCallback);
    }

    @OnClick(R.id.delete)
    public void onDeleteClick() {
        setResult(RoutinesFragment.DELETE_BOOK, intent);
        finish();
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        String name = bookName.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.error_invalid_bookname, Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Wobble).duration(500).delay(100).playOn(bookName);
            return;
        } else {
            intent.putExtra(RoutinesAdapter.BOOK_NAME, name);
            intent.putExtra(RoutinesAdapter.BOOK_ALARM_TIME, alarmTime.getText().toString());
            setResult(RoutinesFragment.RENEW_BOOK, intent);
            finish();
        }
    }

    private CalendarDialogFragment.CalendarCallback mCalendarFragmentCallback = new CalendarDialogFragment.CalendarCallback() {
        @Override
        public void onDateTimeSet(Date date) {
            mDate = date;
            updateAlarmTimeView();
        }
    };

    private void updateAlarmTimeView() {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        alarmTime.setText(DATE_FORMAT.format(mDate));
        Log.d(TAG, DATE_FORMAT.format(mDate));
    }
}
