package com.agenthun.readingroutine.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.Feedback;
import com.agenthun.readingroutine.datastore.UserData;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/22 下午11:43.
 */
public class FeedbackActivity extends AppCompatActivity {
    private static final String TAG = "FeedbackActivity";

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private Toolbar toolbar;
    private TextView feedBack;
    private Button sendButton;
    private static String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setState(MaterialMenuDrawable.IconState.X);
        toolbar.setTitle(R.string.text_app_feedback);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        feedBack = (TextView) findViewById(R.id.edit_feedback_content);
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = feedBack.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    if (content.equals(msg)) {
                        Snackbar.make(sendButton, R.string.error_repeated_feedback, Snackbar.LENGTH_SHORT).show();
                        YoYo.with(Techniques.Shake).duration(500).delay(100).playOn(feedBack);
                    } else {
                        msg = content;
                        sendMessage(content);// 发送反馈信息
                    }
                } else {
                    Snackbar.make(sendButton, R.string.error_invalid_feedback, Snackbar.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake).duration(500).delay(100).playOn(feedBack);
                }
            }
        });
    }

    private void sendMessage(String content) {
        BmobPushManager pushManager = new BmobPushManager(this);
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("isDeveloper", true);
        pushManager.setQuery(query);
        pushManager.pushMessage(content);
        saveFeedbackMsg(content);
    }

    private void saveFeedbackMsg(String content) {
        Feedback feedback = new Feedback();
        String name = (String) UserData.getObjectByKey(this, "username");
        if (name == null || name.length() == 0) {
            name = "trial_user";
        }
        feedback.setUsername(name);
        feedback.setContent(content);
        feedback.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Snackbar.make(sendButton, R.string.msg_save_feedback_success, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Snackbar.make(sendButton, getString(R.string.msg_save_feedback_fail) + s, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
