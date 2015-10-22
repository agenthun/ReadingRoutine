package com.agenthun.readingroutine.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.transitionmanagers.TActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agent Henry on 2015/7/25.
 */
public class ReadingActivity extends TActivity {
    @InjectView(R.id.layout_inner_shortcut)
    LinearLayout layoutInnerShortcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reading);
        ButterKnife.inject(this);

        int[] shortcut = new int[]{
                R.drawable.ic_decrease_font,
                R.drawable.ic_increase_font,
                R.drawable.ic_reading_background,
                R.drawable.ic_marking,
                R.drawable.ic_copy,
                R.drawable.ic_reading_list,
                R.drawable.ic_reading_open
        };

        ImageButton imageButton;
        for (int i = 0; i < shortcut.length; i++) {
            imageButton = (ImageButton) getLayoutInflater().inflate(R.layout.button_shortcut, layoutInnerShortcut, false);
            imageButton.setImageResource(shortcut[i]);
            layoutInnerShortcut.addView(imageButton);
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }
}
