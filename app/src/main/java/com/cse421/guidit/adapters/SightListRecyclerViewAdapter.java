package com.cse421.guidit.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse421.guidit.R;
import com.cse421.guidit.activities.SightDetailActivity;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.SightListVo;
import com.cse421.guidit.vo.SightVo;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hokyung on 2017. 5. 9..
 */

public class SightListRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<SightVo> sightList;
    private SimpleListClickEventListener listener;

    public SightListRecyclerViewAdapter(ArrayList<SightVo> sightList, Context context, SimpleListClickEventListener listener) {
        this.context = context;
        this.sightList = sightList;
        this.listener = listener;
    }
    
    public void setList(ArrayList<SightVo> sightList) {
        this.sightList = sightList;
    }
    
    public ArrayList<SightVo> getList() {
        return sightList;
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sightlist, parent, false);
        return new SightListViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SightListViewHolder viewHolder = (SightListViewHolder) holder;
        // TODO -- 썸네일 수정
        viewHolder.thumbnail.setImageResource(R.drawable.logo);
        viewHolder.title.setText(sightList.get(position).getName());
        viewHolder.subtitle.setText(sightList.get(position).getName());

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClicked(position);
//                if (previousViewHolder != null) {
//                    previousViewHolder.layout.setBackgroundColor(previousViewHolder.basicGray);
//                }
//                viewHolder.layout.setBackgroundColor(viewHolder.selectedColor);
//                previousViewHolder = viewHolder;
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return sightList.size();
    }
    
    public static class SightListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail_sight) ImageView thumbnail;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subtitle) TextView subtitle;
        
        public SightListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
