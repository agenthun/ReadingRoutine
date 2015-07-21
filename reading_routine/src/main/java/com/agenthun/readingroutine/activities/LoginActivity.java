package com.agenthun.readingroutine.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.UserData;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    public static String BMOB_APP_ID = "cc4a89ea058246d6693bcc479b1951e2";

    @InjectView(R.id.login_name)
    EditText loginName;
    @InjectView(R.id.login_password)
    EditText loginPassword;

    public UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化Bmob
        Bmob.initialize(this, BMOB_APP_ID);

        ButterKnife.inject(this);

        userData = UserData.getCurrentUser(this, UserData.class);
        if (userData != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            userData = new UserData();
        }
    }

    @OnClick(R.id.sign_in_button)
    public void onSignInBtnClick() {
        String name = loginName.getText().toString();
        String password = loginPassword.getText().toString();

        userData.setUsername(name);
        userData.setPassword(password);
        userData.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



