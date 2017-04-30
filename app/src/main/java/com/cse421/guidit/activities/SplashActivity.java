package com.cse421.guidit.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cse421.guidit.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        startActivity(LoginActivity.getIntent(SplashActivity.this));
                    }
                },
                2000
        );
    }
}
