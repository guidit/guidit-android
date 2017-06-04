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
import com.cse421.guidit.vo.FestivalVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by ho on 2017-06-04.
 */

public class FestivalRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<FestivalVo> festivalList;
    private SimpleListClickEventListener listener;

    public FestivalRecyclerAdapter(Context context, SimpleListClickEventListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setFestivalList(ArrayList<FestivalVo> festivalList) {
        this.festivalList = festivalList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_festival, parent, false);
        return new FestivalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FestivalViewHolder viewHolder = (FestivalViewHolder) holder;
        final FestivalVo festivalVo = festivalList.get(position);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClicked(position);
            }
        });
        Timber.d("festivalVo : " + festivalVo);
        if (festivalVo.getPicture().equals("")) {
            Picasso.with(context)
                    .load(R.drawable.empty_image)
                    .resize(500, 500)
                    .centerInside()
                    .into(viewHolder.image);
        } else {
            Picasso.with(context)
                    .load(festivalVo.getPicture())
                    .resize(500, 500)
                    .centerCrop()
                    .into(viewHolder.image);
        }
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (festivalVo.getPicture().equals("")) {
                    Picasso.with(context)
                            .load(R.drawable.empty_image)
                            .resize(500, 500)
                            .centerInside()
                            .into(viewHolder.image);
                } else {
                    Picasso.with(context)
                            .load(festivalVo.getPicture())
                            .resize(500, 500)
                            .centerCrop()
                            .into(viewHolder.image);
                }
            }
        });
        viewHolder.name.setText(festivalVo.getName());
        viewHolder.date.setText("기간: " + festivalVo.getDate());
        viewHolder.score.setText(festivalVo.getScore() + "");
    }

    @Override
    public int getItemCount() {
        return festivalList.size();
    }

    public class FestivalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.festival_container) LinearLayout container;
        @BindView(R.id.festival_img) ImageView image;
        @BindView(R.id.festival_name) TextView name;
        @BindView(R.id.festival_date) TextView date;
        @BindView(R.id.festival_score) TextView score;


        public FestivalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
