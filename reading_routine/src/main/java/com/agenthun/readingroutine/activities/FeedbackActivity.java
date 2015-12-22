package com.agenthun.readingroutine.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.agenthun.readingroutine.R;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/22 下午11:43.
 */
public class FeedbackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
