package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleEventListener;
import com.cse421.guidit.connections.LoginConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hokyung on 2017. 4. 30..
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_id) EditText inputId;
    @BindView(R.id.login_passwd) EditText inputPassword;

    public static Intent getIntent (Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_btn)
    public void login(View view) {
        String id = inputId.getText().toString();
        String password = inputPassword.getText().toString();
    
        LoginConnection connection = new LoginConnection();
        connection.setListener(new SimpleEventListener() {
            @Override
            public void connectionSuccess() {
                startActivity(MainActivity.getIntent(LoginActivity.this));
                finish();
            }
    
            @Override
            public void connectionFailed() {
                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(id, password);
    }

    @OnClick(R.id.sign_up_btn)
    public void signUp(View view) {
        startActivity(SignUpActivity.getIntent(LoginActivity.this));
    }
}
