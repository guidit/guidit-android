package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.PlanRecyclerAdapter;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.DailyPlanVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlanActivity extends AppCompatActivity {

    @BindView(R.id.plan_name) EditText nameInput;
    @BindView(R.id.plan_recycler) RecyclerView planRecyclerView;

    private ArrayList<ArrayList<DailyPlanVo>> dailyList;
    private PlanRecyclerAdapter adapter;

    public static Intent getIntent (Context context) {
        return new Intent(context, PlanActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ButterKnife.bind(this);

        setViews();
    }

    private void setViews () {
        //// TODO: 2017-06-03 여행계획 수정으로 들어왔을 때의 동작 추가
        dailyList = new ArrayList<>();
        dailyList.add(new ArrayList<DailyPlanVo>());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new PlanRecyclerAdapter(
                this,
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        // 일정 추가하기 클릭한 경우
                        addSight(position);
                    }
                }
        );
        adapter.setList(dailyList);
        planRecyclerView.setHasFixedSize(true);
        planRecyclerView.setAdapter(adapter);
        planRecyclerView.setLayoutManager(layoutManager);
    }

    private void addSight (int position) {
        //todo 각 날짜에서 일정 추가하기 누른 경우
    }

    @OnClick(R.id.add_date_btn)
    public void addDate () {
        //// TODO: 2017-06-03 날짜 추가 기능
        dailyList.add(new ArrayList<DailyPlanVo>());
    }
}
