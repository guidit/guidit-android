package com.cse421.guidit.fragments;

import android.content.Intent;
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
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.activities.MainActivity;
import com.cse421.guidit.activities.WriteFeedActivity;
import com.cse421.guidit.adapters.FeedRecyclerViewAdapter;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.FeedVo;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class FeedFragment extends Fragment {

    private final int REQ_WRITE_FEED = 119;

    @BindView(R.id.feed_spinner) AppCompatSpinner feedSpinner;
    @BindView(R.id.feed_recycler) RecyclerView feedRecyclerView;

    @BindArray(R.array.upper_locations) String [] locations;

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
        ArrayList<String> list = new ArrayList<>(Arrays.asList(locations));
        list.add(0, "전국");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                list
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedSpinner.setAdapter(adapter);
        feedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //// TODO: 2017-05-29 선택된 도시에대해 connection 전국은 -1
                Toast.makeText(getActivity(), "" + (i - 1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 선택 없으면 아무것도 하지 않는다
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
        startActivityForResult(WriteFeedActivity.getIntent(getActivity()), REQ_WRITE_FEED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_WRITE_FEED)
            if (resultCode == RESULT_OK) {
                //// TODO: 2017-06-01 spinner 사용자가 작성한 지역으로 바꾸고, 피드 새로고침
            }
    }
}
