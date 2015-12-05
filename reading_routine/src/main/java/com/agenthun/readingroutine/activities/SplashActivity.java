package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.fragments.SettingsFragment;

/**
 * Created by Agent Henry on 2015/7/25.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String IS_FIRST = "IS_FIRST";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            if (getIsFirst()) {
                intent.setClass(getApplicationContext(), LoginActivity.class);
            } else {
                intent.setClass(getApplicationContext(), LoginActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.sendEmptyMessageDelayed(0, 1500);
    }

    public void setIsFirst(boolean isFirst) {
        getApplicationContext().getSharedPreferences(SettingsFragment.GLOBAL_SETTINGS, 0)
                .edit().putBoolean(IS_FIRST, isFirst);
    }

    public boolean getIsFirst() {
        return getApplicationContext().getSharedPreferences(SettingsFragment.GLOBAL_SETTINGS, 0)
                .getBoolean(IS_FIRST, true);
    }
}
