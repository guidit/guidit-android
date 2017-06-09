package com.cse421.guidit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.util.ImageUtil;
import com.cse421.guidit.vo.PlanVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hokyung on 2017. 5. 10..
 */

public class MyPagePlanRecyclerAdapter extends RecyclerView.Adapter {
    
    private Context context;
    private ArrayList<PlanVo> planList;
    private SimpleListClickEventListener listener;
    
    public MyPagePlanRecyclerAdapter(Context context, SimpleListClickEventListener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setPlanList(ArrayList<PlanVo> planList) {
        this.planList = planList;
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mypage_plan, parent, false);
        
        return new PlanViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PlanViewHolder planViewHolder = (PlanViewHolder) holder;
        PlanVo planVo = planList.get(position);

        Picasso.with(context)
                .load(ImageUtil.getTravelImageId())
                .resize(500, 300)
                .centerCrop()
                .into(planViewHolder.image);
        
        planViewHolder.planName.setText(planVo.getName());
        if (planVo.isPublic()) {
            planViewHolder.lock.setVisibility(View.GONE);
        } else {
            planViewHolder.lock.setVisibility(View.VISIBLE);
        }
        
        planViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClicked(position);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return planList.size();
    }
    
    public class PlanViewHolder extends RecyclerView.ViewHolder {
        
        @BindView(R.id.relative_layout) RelativeLayout layout;
        @BindView(R.id.plan_image) ImageView image;
        @BindView(R.id.plan_name) TextView planName;
        @BindView(R.id.lock) ImageView lock;
        
        public PlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
