package com.cse421.guidit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.SightVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ho on 2017-06-04.
 */

public class DailyPlanRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<SightVo> sightList;
    private SimpleListClickEventListener listener;
    private From where;
    private Mode mode;

    public enum From {
        PlanActivity, ReviewActivity
    }

    public enum Mode {
        MY, OTHER
    }

    public DailyPlanRecyclerViewAdapter(Context context, From where, Mode mode) {
        this.context = context;
        this.where = where;
        this.mode = mode;
    }

    public void setListener(SimpleListClickEventListener listener) {
        this.listener = listener;
    }

    public void setSightList(ArrayList<SightVo> sightList) {
        this.sightList = sightList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daily_plan, parent, false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DailyViewHolder viewHolder = (DailyViewHolder) holder;
        SightVo sightVo = sightList.get(position);

        viewHolder.name.setText(sightVo.getName());
        if (where == From.ReviewActivity) {
            viewHolder.deleteButton.setVisibility(View.GONE);
        } else {
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClicked(position);
                }
            });
        }
        if (mode == Mode.OTHER) {
            viewHolder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return sightList.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sight_name) TextView name;
        @BindView(R.id.delete_btn) ImageView deleteButton;

        public DailyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
