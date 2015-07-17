package com.agenthun.readingroutine.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.fragments.CalendarDialogFragment;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import java.util.Arrays;
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
/*    @InjectView(R.id.alarm_time)
    TextView alarmTime;*/

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
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.text_book));

        int[] colorBook = getResources().getIntArray(R.array.style_book_color);
        toolbar.setBackgroundColor(colorBook[new Random().nextInt(4)]);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenuIconToolbar.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
                finish();
            }
        });
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
