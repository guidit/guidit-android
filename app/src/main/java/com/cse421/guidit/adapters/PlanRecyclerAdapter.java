package com.cse421.guidit.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.PlanClickEventListener;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.DailyPlanVo;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by ho on 2017-06-03.
 */

public class PlanRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<DailyPlanVo> list;
    private PlanClickEventListener listener;
    private ArrayList<DailyPlanRecyclerViewAdapter> adapterList;
    private Mode mode;

    public enum Mode {
        NEW, EXIST, OTHER
    }

    public PlanRecyclerAdapter(Context context, PlanClickEventListener listener) {
        this.context = context;
        this.listener = listener;
        adapterList = new ArrayList<>();
    }

    public void setList(ArrayList<DailyPlanVo> list) {
        this.list = list;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
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

        if (adapterList.size() <= position) {
            final DailyPlanRecyclerViewAdapter adapter = new DailyPlanRecyclerViewAdapter(context, DailyPlanRecyclerViewAdapter.from.PlanActivity);
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

        if (mode == Mode.NEW || item.getPicture().equals("null")) {
            planViewHolder.reviewImage.setVisibility(GONE);
        } else {
            planViewHolder.reviewImage.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(item.getPicture())
                    .into(planViewHolder.reviewImage);
        }

        if (mode == Mode.NEW || item.getReview().equals("null")) {
            planViewHolder.review.setVisibility(GONE);
        } else {
            planViewHolder.review.setText(item.getReview());
            planViewHolder.review.setVisibility(View.VISIBLE);
        }

        if (mode != Mode.OTHER) {
            planViewHolder.changeDailyPlanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.changeDailyPlan(position);
                }
            });
            planViewHolder.writeReviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.writeReview(position);
                }
            });
        }

        if (mode == Mode.EXIST) {
            planViewHolder.writeReviewButton.setVisibility(View.VISIBLE);
        } else if (mode == Mode.OTHER) {
            planViewHolder.changeDailyPlanButton.setVisibility(GONE);
            planViewHolder.writeReviewButton.setVisibility(GONE);
        } else {
            planViewHolder.writeReviewButton.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void changeInnerRecycler(int position, ArrayList<SightVo> sightList) {
        adapterList.get(position).setSightList(sightList);
        adapterList.get(position).notifyDataSetChanged();
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_count) TextView dateCount;
        @BindView(R.id.daily_plan_recycler) RecyclerView dailyRecyclerView;
        @BindView(R.id.review_image) ImageView reviewImage;
        @BindView(R.id.review) TextView review;
        @BindView(R.id.change_daily_plan) TextView changeDailyPlanButton;
        @BindView(R.id.write_review) TextView writeReviewButton;

        public PlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
