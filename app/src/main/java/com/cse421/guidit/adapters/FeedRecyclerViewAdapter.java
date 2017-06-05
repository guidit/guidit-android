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
import com.cse421.guidit.vo.FeedVo;
import com.cse421.guidit.vo.UserVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.view.View.GONE;

/**
 * Created by ho on 2017-05-29.
 */

public class FeedRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<FeedVo> feedList;
    private SimpleListClickEventListener listener;

    public FeedRecyclerViewAdapter(Context context, SimpleListClickEventListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setFeedList(ArrayList<FeedVo> feedList) {
        this.feedList = feedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);

        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        FeedViewHolder feedViewHolder = (FeedViewHolder) holder;
        FeedVo feedVo = feedList.get(position);

        if (!feedVo.getProfile().equals("testprofile"))
            Picasso.with(context)
                    .load(feedVo.getProfile())
                    .resize(800, 600)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(feedViewHolder.profile);

        feedViewHolder.name.setText(feedVo.getUserName());

        feedViewHolder.content.setText(feedVo.getContent());

        if (feedVo.getUserId() == UserVo.getInstance().getId()) {
            feedViewHolder.deleteButton.setVisibility(View.VISIBLE);
            feedViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClicked(position);
                }
            });
        } else {
            feedViewHolder.deleteButton.setVisibility(GONE);
            feedViewHolder.deleteButton.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_img) ImageView profile;
        @BindView(R.id.user_name) TextView name;
        @BindView(R.id.feed_content) TextView content;
        @BindView(R.id.feed_delete) ImageView deleteButton;

        public FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
