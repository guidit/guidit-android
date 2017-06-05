package com.cse421.guidit.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
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
    private ArrayList<DailyPlanVo> list;
    private SimpleListClickEventListener listener;
    private ArrayList<DailyPlanRecyclerViewAdapter> adapterList;

    public PlanRecyclerAdapter(Context context, SimpleListClickEventListener listener) {
        this.context = context;
        this.listener = listener;
        adapterList = new ArrayList<>();
    }

    public void setList(ArrayList<DailyPlanVo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PlanViewHolder planViewHolder = (PlanViewHolder) holder;
        final DailyPlanVo item = list.get(position);

        planViewHolder.dateCount.setText((position + 1) + "일차");
        planViewHolder.addDailyPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClicked(position);
            }
        });

        if (adapterList.size() <= position) {
            final DailyPlanRecyclerViewAdapter adapter = new DailyPlanRecyclerViewAdapter(context);
            adapter.setSightList(item.getSightList());
            adapter.setListener(new SimpleListClickEventListener() {
                @Override
                public void itemClicked(int position) {
                    item.getSightList().remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            adapterList.add(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            planViewHolder.dailyRecyclerView.setHasFixedSize(true);
            planViewHolder.dailyRecyclerView.setAdapter(adapterList.get(position));
            planViewHolder.dailyRecyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notifyInnerAdapter (int position) {
        adapterList.get(position).notifyDataSetChanged();
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
