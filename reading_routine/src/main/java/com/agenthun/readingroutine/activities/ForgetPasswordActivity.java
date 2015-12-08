package com.agenthun.readingroutine.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.UserData;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import cn.bmob.v3.listener.ResetPasswordByEmailListener;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/8 上午1:41.
 */
public class ForgetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgetPasswordActivity";

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private Toolbar toolbar;
    private TextView emailAddress;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setState(MaterialMenuDrawable.IconState.X);
        toolbar.setTitle(R.string.text_reset_password);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailAddress = (TextView) findViewById(R.id.email_address);
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailAddress.getText().toString().trim();
                UserData.resetPasswordByEmail(ForgetPasswordActivity.this, email, new ResetPasswordByEmailListener() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(ForgetPasswordActivity.this, "重置密码请求成功, 请到" + email + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                        Snackbar.make(sendButton, "重置密码请求成功, 请到" + email + "邮箱进行密码重置操作", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
//                        Toast.makeText(ForgetPasswordActivity.this, "重置密码失败: " + s, Toast.LENGTH_SHORT).show();
                        Snackbar.make(sendButton, "重置密码失败: " + s, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
