package com.cse421.guidit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.SightListVo;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hokyung on 2017. 5. 9..
 */

public class SightListRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<SightListVo> items;
    private SimpleListClickEventListener listener;

    public SightListRecyclerViewAdapter(ArrayList<SightListVo> items, Context context) {
        this.context = context;
        this.items = items;
    }
    
    public void setList(ArrayList<SightListVo> items) {
        this.items = items;
    }
    
    public ArrayList<SightListVo> getList() {
        return items;
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sightlist, parent, false);
        return new SightListViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SightListViewHolder viewHolder = (SightListViewHolder) holder;
        viewHolder.thumbnail.setImageResource(items.get(position).image);
        viewHolder.title.setText(items.get(position).title);
        viewHolder.subtitle.setText(items.get(position).subtitle);
    }
    
    @Override
    public int getItemCount() {
        return items.size();
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
