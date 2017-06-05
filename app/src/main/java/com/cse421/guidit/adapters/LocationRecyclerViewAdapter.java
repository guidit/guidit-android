package com.cse421.guidit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hokyung on 2017. 5. 9..
 */

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter {
    
    private Context context;
    private ArrayList<String> list;
    private SimpleListClickEventListener listener;
    private LocationViewHolder previousViewHolder;
    
    public LocationRecyclerViewAdapter(Context context, SimpleListClickEventListener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setList(ArrayList<String> list) {
        this.list = list;
    }
    
    public ArrayList<String> getList() {
        return list;
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final LocationViewHolder viewHolder = (LocationViewHolder) holder;
        viewHolder.location.setText(list.get(position));
        viewHolder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClicked(position);
                if (previousViewHolder != null) {
                    previousViewHolder.layout.setBackgroundColor(previousViewHolder.basicGray);
                    previousViewHolder.location.setTextColor(previousViewHolder.black);
                }
                viewHolder.layout.setBackgroundColor(viewHolder.white);
                viewHolder.location.setTextColor(viewHolder.selectedColor);
                previousViewHolder = viewHolder;
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return list.size();
    }
    
    public void customDataSetChange () {
        if (previousViewHolder != null) {
            previousViewHolder.layout.setBackgroundColor(previousViewHolder.basicGray);
            previousViewHolder.location.setTextColor(previousViewHolder.black);
        }
        notifyDataSetChanged();
    }
    
    public static class LocationViewHolder extends RecyclerView.ViewHolder {
    
        @BindView(R.id.linear_layout) LinearLayout layout;
        @BindView(R.id.location) TextView location;
        @BindColor(R.color.gray) int basicGray;
        @BindColor(R.color.white) int white;
        @BindColor(R.color.colorMainOrange) int selectedColor;
        @BindColor(R.color.black) int black;
        
        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
