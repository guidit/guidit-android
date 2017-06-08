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
import com.cse421.guidit.callbacks.FavoriteClickEventListener;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ho on 2017-05-21.
 */

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<SightVo> items;
    private FavoriteClickEventListener listener;

    public FavoriteRecyclerViewAdapter(Context context, ArrayList<SightVo> items) {
        this.context = context;
        this.items = items;
    }

    public void setList(ArrayList<SightVo> items) {
        this.items = items;
    }

    public void setListener(FavoriteClickEventListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        FavoriteViewHolder viewHolder = (FavoriteViewHolder) holder;
        SightVo sightVo = items.get(position);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClicked(position);
            }
        });
        Picasso.with(context)
                .load(sightVo.getPicture())
                .into(viewHolder.image);
        viewHolder.name.setText(sightVo.getName());
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemDeleted(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.favorite_layout) RelativeLayout layout;
        @BindView(R.id.sight_img) ImageView image;
        @BindView(R.id.sight_name) TextView name;
        @BindView(R.id.favorite_delete_btn) ImageView deleteButton;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
