package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cse421.guidit.R;

import butterknife.ButterKnife;

public class WriteFeedActivity extends AppCompatActivity {

    public static Intent getIntent (Context context) {
        return new Intent(context, WriteFeedActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_feed);
        //setResult(RESULT_OK);

        ButterKnife.bind(this);
    }
}
