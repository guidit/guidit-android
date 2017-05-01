package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.profile_img) ImageView profileImage;
    @BindView(R.id.sign_up_name) EditText inputName;
    @BindView(R.id.sign_up_id) EditText inputId;
    @BindView(R.id.sign_up_password) EditText inputPassword;
    @BindView(R.id.password_confirm) EditText inputPassword2;

    public static Intent getIntent (Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.profile_img)
    public void getImage (View view) {
        //// TODO: 2017. 5. 1. 갤러리에서 이미지 가져오기
        Toast.makeText(this, "갤리리 ㄱ", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.sign_up_confirm)
    public void submit (View view) {
        // // TODO: 2017. 5. 1. 회원가입 제출 시 동작
        String name = inputName.getText().toString();
        String id = inputId.getText().toString();
        String password = inputPassword.getText().toString();
        String password2 = inputPassword2.getText().toString();
        Toast.makeText(this, name + "/" + id + "/" + password + "/" + password2, Toast.LENGTH_SHORT).show();

        // 비밀번호 확인
        if (!password.equals(password2)) {
            Toast.makeText(this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(LoginActivity.getIntent(SignUpActivity.this));
        finish();
    }
}
