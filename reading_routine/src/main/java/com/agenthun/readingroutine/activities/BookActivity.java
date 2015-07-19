package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.RoutinesAdapter;
import com.agenthun.readingroutine.fragments.CalendarDialogFragment;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BookActivity extends TActivity {

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.book_name_edittext)
    EditText bookName;
    @InjectView(R.id.alarm_time)
    TextView alarmTime;

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
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.color_white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.color_white));

        Intent intent = getIntent();
        String getBookName = intent.getStringExtra(RoutinesAdapter.BOOK_NAME);
        collapsingToolbarLayout.setTitle(getBookName);

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

        if (getBookName.compareTo(getResources().getString(R.string.text_book)) != 0) {
            bookName.setText(getBookName);
        }

        String getBookAlarmTime = intent.getStringExtra(RoutinesAdapter.BOOK_ALARM_TIME);
        alarmTime.setText(getBookAlarmTime);
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @OnClick(R.id.alarm_time)
    public void onAlarmTimeClick() {
        System.out.println("onAlarmTimeClick");
        new CalendarDialogFragment();
    }
}
