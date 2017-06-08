package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.MyPagePlanRecyclerAdapter;
import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.connections.PlanConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.PlanVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherPlanActivity extends AppCompatActivity {

    @BindView(R.id.other_plan_recycler) RecyclerView planRecyclerView;

    private ArrayList<PlanVo> planList;
    private MyPagePlanRecyclerAdapter adapter;

    public static Intent getIntent (Context context) {
        return new Intent(context, OtherPlanActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_plan);

        ButterKnife.bind(this);

        getData();
    }

    private void getData() {
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();
        final PlanConnection connection = new PlanConnection(PlanConnection.modes.ALL_PLANS);
        connection.setListConnectionListener(new ListConnectionListener<PlanVo>() {
            @Override
            public void setList(ArrayList<PlanVo> list) {
                progressBar.cancel();
                planList = list;
                setRecyclerView();
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(OtherPlanActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void listIsEmpty() {
                connectionFailed();
            }
        });
        connection.execute();
    }

    private void setRecyclerView () {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        
        adapter = new MyPagePlanRecyclerAdapter(this, new SimpleListClickEventListener() {
            @Override
            public void itemClicked(int position) {
                Intent intent = PlanActivity.getIntent(OtherPlanActivity.this);
                intent.putExtra("planId", planList.get(position).getId());
                intent.putExtra("other", 0);
                startActivity(intent);
            }
        });
        adapter.setPlanList(planList);

        planRecyclerView.setHasFixedSize(true);
        planRecyclerView.setAdapter(adapter);
        planRecyclerView.setLayoutManager(layoutManager);
    }
}
