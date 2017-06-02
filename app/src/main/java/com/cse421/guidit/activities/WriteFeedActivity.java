package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.FeedConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class WriteFeedActivity extends AppCompatActivity {

    @BindView(R.id.feed_spinner) AppCompatSpinner spinner;
    @BindView(R.id.feed_input) EditText feedInput;

    private int selectedCity;

    public static Intent getIntent (Context context) {
        return new Intent(context, WriteFeedActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_feed);

        ButterKnife.bind(this);

        setViews();
    }

    private void setViews () {
        selectedCity = getIntent().getIntExtra("city", 0);
        if (selectedCity < 0)
            selectedCity = 0;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.upper_locations,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
        spinner.setSelection(selectedCity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.write_feed_btn)
    public void writeFeed () {
        if (feedInput.getText().toString().length() == 0) {
            Toast.makeText(this, "내용이 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressBarDialogUtil progressBarDialogUtil = new ProgressBarDialogUtil(this);
        progressBarDialogUtil.show();

        FeedConnection connection = new FeedConnection(FeedConnection.CREATE);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                progressBarDialogUtil.cancel();
                Intent intent = new Intent();
                intent.putExtra("city", selectedCity + 1);
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(WriteFeedActivity.this, "입력되었습니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectionFailed() {
                progressBarDialogUtil.cancel();
                Toast.makeText(WriteFeedActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(
                feedInput.getText().toString(),
                (selectedCity + 1) + ""
        );
    }
}
