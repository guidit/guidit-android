package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cse421.guidit.R;

import butterknife.ButterKnife;

/**
 * Created by hokyung on 2017. 4. 30..
 */

public class LoginActivity extends AppCompatActivity {

    public static Intent getIntent (Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }
}
