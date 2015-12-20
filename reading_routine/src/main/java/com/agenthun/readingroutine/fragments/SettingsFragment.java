package com.agenthun.readingroutine.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.transitionmanagers.TFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends TFragment {

    public static final String GLOBAL_SETTINGS = "MR_GLOBAL_SETTINGS";
    private static final String FONT_SIZE = "FONT_SIZE";
    private static final String BRIGHTNESS = "BRIGHTNESS";
    private static final String DAYTIME_MODE_ON = "DAYTIME_MODE_ON";

    @InjectView(R.id.seekBarSetFontSize)
    SeekBar seekBarSetFontSize;
    @InjectView(R.id.seekBarBrightness)
    SeekBar seekBarBrightness;
    @InjectView(R.id.switchReadingMode)
    SwitchCompat switchReadingMode;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, view);
        updateUIFromStoredSettings();
        setupUserInteraction();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, "设置功能 - 敬请期待", Snackbar.LENGTH_SHORT).show();
            }
        }, 500);
        return view;
    }

    private void updateUIFromStoredSettings() {
        seekBarSetFontSize.setProgress(getFontSize(getContext().getApplicationContext()));
        seekBarBrightness.setProgress(getBrightness(getContext().getApplicationContext()));
        switchReadingMode.setChecked(getReadingMode(getContext().getApplicationContext()));
    }

    private void setupUserInteraction() {
        setSeekBarSetFontSizeInteraction();
        setSeekBarBrightnessInteraction();
        setSwitchReadingModeInteraction();
    }

    private void setSeekBarSetFontSizeInteraction() {
        seekBarSetFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setFontSize(getContext().getApplicationContext(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSeekBarBrightnessInteraction() {
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBrightness(getContext().getApplicationContext(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSwitchReadingModeInteraction() {
        switchReadingMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setReadingMode(getContext().getApplicationContext(), switchReadingMode.isChecked());
            }
        });
    }

    private void setFontSize(Context context, int progress) {
        context.getSharedPreferences(GLOBAL_SETTINGS, 0).edit().putInt(FONT_SIZE, progress).commit();
    }

    private void setBrightness(Context context, int progress) {
        context.getSharedPreferences(GLOBAL_SETTINGS, 0).edit().putInt(BRIGHTNESS, progress).commit();
    }

    private void setReadingMode(Context context, boolean checked) {
        context.getSharedPreferences(GLOBAL_SETTINGS, 0).edit().putBoolean(DAYTIME_MODE_ON, checked).commit();
    }

    public static int getFontSize(Context context) {
        return context.getSharedPreferences(GLOBAL_SETTINGS, 0).getInt(FONT_SIZE, 50);
    }

    public static int getBrightness(Context context) {
        return context.getSharedPreferences(GLOBAL_SETTINGS, 0).getInt(BRIGHTNESS, 50);
    }

    public static boolean getReadingMode(Context context) {
        return context.getSharedPreferences(GLOBAL_SETTINGS, 0).getBoolean(DAYTIME_MODE_ON, false);
    }

}
