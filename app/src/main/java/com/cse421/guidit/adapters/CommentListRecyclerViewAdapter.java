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
import com.cse421.guidit.util.CircleTransform;
import com.cse421.guidit.vo.CommentVo;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeongyi on 2017. 6. 4..
 */

public class CommentListRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<CommentVo> commentList;

    public CommentListRecyclerViewAdapter(ArrayList<CommentVo> commentList, Context context) {
        this.context = context;
        this.commentList = commentList;
    }
    
    public void setList(ArrayList<CommentVo> commentList) {
        this.commentList = commentList;
    }
    
    public ArrayList<CommentVo> getList() {
        return commentList;
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new SightListViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SightListViewHolder viewHolder = (SightListViewHolder) holder;
        if (!commentList.get(position).getUserProfile().equals("null"))
            Picasso.with(context)
                    .load(commentList.get(position).getUserProfile())
                    .resize(800, 600)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(viewHolder.profile);
        viewHolder.userId.setText(commentList.get(position).getUserId());
        viewHolder.content.setText(commentList.get(position).getComment()+"");
        viewHolder.date.setText(commentList.get(position).getDate()+"");

    }
    
    @Override
    public int getItemCount() {
        return commentList.size();
    }
    
    public static class SightListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile) ImageView profile;
        @BindView(R.id.userId) TextView userId;
        @BindView(R.id.content) TextView content;
        @BindView(R.id.date) TextView date;
        
        public SightListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
