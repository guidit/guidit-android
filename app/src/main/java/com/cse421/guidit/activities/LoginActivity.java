package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.LoginConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hokyung on 2017. 4. 30..
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_id) EditText inputId;
    @BindView(R.id.login_passwd) EditText inputPassword;
    @BindView(R.id.login_btn) Button loginButton;
    @BindView(R.id.sign_up_btn) TextView signUpButton;
    @BindView(R.id.test_btn) Button testButton;

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
    
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();
    
        LoginConnection connection = new LoginConnection();
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                progressBar.cancel();
                startActivity(MainActivity.getIntent(LoginActivity.this));
                finish();
            }
    
            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(id, password);
    }

    @OnClick(R.id.sign_up_btn)
    public void signUp(View view) {
        startActivity(SignUpActivity.getIntent(LoginActivity.this));
    }
    
    //// TODO: 2017. 5. 8. test용 버튼
    @OnClick(R.id.test_btn)
    public void test(View view) {
        startActivity(MainActivity.getIntent(LoginActivity.this));
        finish();
    }
}
