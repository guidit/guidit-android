package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.FestivalRecyclerAdapter;
import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.connections.FestivalConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.FestivalVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FestivalActivity extends AppCompatActivity {

    @BindView(R.id.festival_month) TextView monthTitle;
    @BindView(R.id.festival_recycler) RecyclerView festivalRecyclerView;

    private int currentMonth;
    private ArrayList<FestivalVo> festivalList;
    private FestivalRecyclerAdapter adapter;

    public static Intent getIntent (Context context) {
        return new Intent(context, FestivalActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        ButterKnife.bind(this);

        setViews();
        getFestivalList();
    }

    private void setViews () {
        Intent data = getIntent();
        currentMonth = data.getIntExtra("month", 1);
        monthTitle.setText(currentMonth + "월 축제");

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");
        monthTitle.setTypeface(type);

        festivalList = new ArrayList<>();
        adapter = new FestivalRecyclerAdapter(
                this,
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                    }
                }
        );
        adapter.setFestivalList(festivalList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        festivalRecyclerView.setHasFixedSize(true);
        festivalRecyclerView.setAdapter(adapter);
        festivalRecyclerView.setLayoutManager(layoutManager);
    }

    private void getFestivalList() {
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();
        FestivalConnection connection = new FestivalConnection();
        connection.setListConnectionListener(new ListConnectionListener() {
            @Override
            public void setList(ArrayList list) {
                progressBar.cancel();
                festivalList = list;
                adapter.setFestivalList(festivalList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(FestivalActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(currentMonth + "");
    }
}
