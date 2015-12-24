package com.agenthun.readingroutine.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.activities.FeedbackActivity;
import com.agenthun.readingroutine.transitionmanagers.TFragment;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends TFragment {

    private static final String TAG = "AboutFragment";

    private MaterialMenuIconToolbar materialMenuIconToolbar;

    private LinearLayout updateVersionArea;
    private TextView appIntroduction;
    private TextView appThanks;
    private TextView appAboutMe;
    private TextView appFeedback;
    private TextView appNewVersionNumHint;
    private TextView appNewVersionNum;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        updateVersionArea = (LinearLayout) view.findViewById(R.id.update_version_area);
        appIntroduction = (TextView) view.findViewById(R.id.app_introduction);
        appThanks = (TextView) view.findViewById(R.id.app_thanks);
        appAboutMe = (TextView) view.findViewById(R.id.app_about_me);
        appFeedback = (TextView) view.findViewById(R.id.app_feedback);
/*        appNewVersionNumHint = (TextView) view.findViewById(R.id.app_new_version_num_hint);
        appNewVersionNum = (TextView) view.findViewById(R.id.app_new_version_num);*/

        updateVersionArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(updateVersionArea, R.string.text_app_already_the_latest_version, Snackbar.LENGTH_SHORT).show();
            }
        });

        appIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_introduction, null);
                builder.setView(view);
                builder.setTitle(R.string.text_app_introduction).setPositiveButton(R.string.text_ok, null).show();
            }
        });

        appThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.text_thanks).setMessage("crush").setPositiveButton(R.string.text_ok, null).show();
            }
        });

        appAboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://agenthun.github.io";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.addCategory("android.intent.category.BROWSABLE");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(appAboutMe, R.string.error_404_network, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        appFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FeedbackActivity.class));
            }
        });
        return view;
    }
}
