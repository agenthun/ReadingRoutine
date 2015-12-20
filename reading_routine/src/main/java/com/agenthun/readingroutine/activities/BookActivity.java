package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.RoutinesAdapter;
import com.agenthun.readingroutine.fragments.CalendarDialogFragment;
import com.agenthun.readingroutine.fragments.RoutinesFragment;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
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
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    @InjectView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.book_name_edittext)
    EditText bookName;
    @InjectView(R.id.alarm_time)
    TextView alarmTime;
    @InjectView(R.id.save)
    Button saveBtn;
    @InjectView(R.id.ic_reading_routine)
    ImageView icon;
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
        //toolbar.setBackgroundColor(colorBook[getBookColorIndex]);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.alpha(colorBook[getBookColorIndex]));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenuIconToolbar.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
                toolbar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
            }
        });

        String getBookName = intent.getStringExtra(RoutinesAdapter.BOOK_NAME);
        collapsingToolbarLayout.setTitle(getBookName);
        appBarLayout.setBackgroundColor(colorBook[getBookColorIndex]);

        if (getBookName.compareTo(getResources().getString(R.string.text_book)) != 0) {
            bookName.setText(getBookName);
        }

        getBookAlarmTime = intent.getStringExtra(RoutinesAdapter.BOOK_ALARM_TIME);
        try {
            mDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").parse(getBookAlarmTime);
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            mDate = calendar.getTime();
        }

        alarmTime.setText(DATE_FORMAT.format(mDate));

        int[] colorFab = getResources().getIntArray(R.array.style_book_fab_color);
        fab.setBackgroundTintList(ColorStateList.valueOf(colorFab[getBookColorIndex]));

/*        fab.setVisibility(View.VISIBLE);
        fab.animate().scaleY(1.0f).scaleX(1.0f).alpha(1.0f)
                .setDuration(200)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setStartDelay(2000)
                .start();*/
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
        CalendarDialogFragment calendarDialogFragment = new CalendarDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(CalendarDialogFragment.SELECTD_DATE, mDate.getTime());
        calendarDialogFragment.setArguments(bundle);
        calendarDialogFragment.show(getSupportFragmentManager(), TAG);
        calendarDialogFragment.setCalendarCallback(mCalendarFragmentCallback);

/*        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d(TAG, "onDateSet() returned: " + year);
                Log.d(TAG, "onDateSet() returned: " + monthOfYear);
                Log.d(TAG, "onDateSet() returned: " + dayOfMonth);
            }
        };
        new DatePickerDialog(BookActivity.this, mDateSetListener, 2015, 11, 29).show();*/
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
            Snackbar.make(saveBtn, R.string.error_invalid_bookname, Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            YoYo.with(Techniques.Wobble).duration(500).delay(100).playOn(bookName);
            return;
        } else {
            intent.putExtra(RoutinesAdapter.BOOK_NAME, name);
            intent.putExtra(RoutinesAdapter.BOOK_ALARM_TIME, getBookAlarmTime);
            setResult(RoutinesFragment.RENEW_BOOK, intent);
            finish();
        }
    }

    @OnClick(R.id.fab)
    public void onFabSaveClick() {
        String name = bookName.getText().toString();
        if (name.isEmpty()) {
            Snackbar.make(fab, R.string.error_invalid_bookname, Snackbar.LENGTH_SHORT).setAction("Error", null).show();
            YoYo.with(Techniques.Wobble).duration(500).delay(100).playOn(bookName);
            return;
        } else {
            intent.putExtra(RoutinesAdapter.BOOK_NAME, name);
            intent.putExtra(RoutinesAdapter.BOOK_ALARM_TIME, getBookAlarmTime);
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
        getBookAlarmTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(mDate);
        alarmTime.setText(DATE_FORMAT.format(mDate));
    }
}
