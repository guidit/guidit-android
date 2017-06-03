package com.cse421.guidit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.DailyPlanVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ho on 2017-06-03.
 */

public class PlanRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ArrayList<DailyPlanVo>> list;
    private SimpleListClickEventListener listener;

    public PlanRecyclerAdapter(Context context, SimpleListClickEventListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setList(ArrayList<ArrayList<DailyPlanVo>> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlanViewHolder planViewHolder = (PlanViewHolder) holder;
        ArrayList<DailyPlanVo> item = list.get(position);

        planViewHolder.dateCount.setText((position + 1) + "일차");
        //// TODO: 2017-06-03 리사이클러 어뎁터 달기
        planViewHolder.addDailyPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //// TODO: 2017-06-03 일정 추가하는 액티비티 띄우도록 만들기
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_count) TextView dateCount;
        @BindView(R.id.daily_plan_recycler) RecyclerView dailyRecyclerView;
        @BindView(R.id.add_daily_plan) TextView addDailyPlanButton;

        public PlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
