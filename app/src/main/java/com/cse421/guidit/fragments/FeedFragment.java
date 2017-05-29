package com.cse421.guidit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.cse421.guidit.R;
import com.cse421.guidit.activities.MainActivity;
import com.cse421.guidit.adapters.FeedRecyclerViewAdapter;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.FeedVo;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class FeedFragment extends Fragment {

    @BindView(R.id.feed_spinner) AppCompatSpinner feedSpinner;
    @BindView(R.id.feed_recycler) RecyclerView feedRecyclerView;

    private ArrayList<FeedVo> feedList;
    private FeedRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        setSpinner();
        setRecyclerView();

        return view;
    }

    private void setSpinner () {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.upper_locations,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedSpinner.setAdapter(adapter);
        feedSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //// TODO: 2017-05-29 선택된 도시에대해 connection
            }
        });
    }

    private void setRecyclerView () {
        // 여행계획 리스트
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        feedList = ((MainActivity) getActivity()).feedList;
        adapter = new FeedRecyclerViewAdapter(
                getActivity(),
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        //// TODO: 2017-05-29 피드 삭제 구현
                    }
                }
        );
        adapter.setFeedList(feedList);
        feedRecyclerView.setHasFixedSize(true);
        feedRecyclerView.setAdapter(adapter);
        feedRecyclerView.setLayoutManager(layoutManager);
    }

    public void createFeed () {
        //// TODO: 2017-05-29 피드 생성 액티비티로
    }
}
