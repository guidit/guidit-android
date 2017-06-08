package com.cse421.guidit.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ho on 2017-06-04.
 */

public class AddSightRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<SightVo> list;

    public AddSightRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<SightVo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_sight, parent, false);
        return new AddSightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AddSightViewHolder viewHolder = (AddSightViewHolder) holder;
        final SightVo sightVo = list.get(position);

        if (sightVo.getPicture().equals("")) {
            Picasso.with(context)
                    .load(R.drawable.empty_image)
                    .into(viewHolder.image);
        } else {
            Picasso.with(context)
                    .load(sightVo.getPicture())
                    .into(viewHolder.image);
        }
        viewHolder.name.setText(sightVo.getName());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sightVo.setChecked(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AddSightViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sight_img) ImageView image;
        @BindView(R.id.sight_name) TextView name;
        @BindView(R.id.sight_check_box) AppCompatCheckBox checkBox;

        public AddSightViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
