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
import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.connections.FeedConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;
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
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                list
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedSpinner.setAdapter(spinnerAdapter);
        feedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final ProgressBarDialogUtil progressBarDialogUtil = new ProgressBarDialogUtil(getActivity());
                progressBarDialogUtil.show();
                FeedConnection connection = new FeedConnection(FeedConnection.GET_LIST);
                connection.setListConnectionListener(new ListConnectionListener<FeedVo>() {
                    @Override
                    public void setList(ArrayList<FeedVo> list) {
                        progressBarDialogUtil.cancel();
                        feedList = list;
                        adapter.setFeedList(feedList);
                        adapter.notifyDataSetChanged();
                    }
                });
                connection.execute(i + "");
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

        feedList = new ArrayList<>();
        adapter = new FeedRecyclerViewAdapter(
                getActivity(),
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        FeedConnection feedConnection = new FeedConnection(FeedConnection.DELETE);
                        feedConnection.setListener(new SimpleConnectionEventListener() {
                            @Override
                            public void connectionSuccess() {
                                Toast.makeText(getActivity(), "삭제되었습니다", Toast.LENGTH_SHORT).show();

                                final ProgressBarDialogUtil progressBarDialogUtil = new ProgressBarDialogUtil(getActivity());
                                progressBarDialogUtil.show();
                                FeedConnection connection = new FeedConnection(FeedConnection.GET_LIST);
                                connection.setListConnectionListener(new ListConnectionListener<FeedVo>() {
                                    @Override
                                    public void setList(ArrayList<FeedVo> list) {
                                        progressBarDialogUtil.cancel();
                                        feedList = list;
                                        adapter.setFeedList(feedList);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                connection.execute(feedSpinner.getFirstVisiblePosition() + "");
                            }

                            @Override
                            public void connectionFailed() {
                                Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                            }
                        });
                        feedConnection.execute(feedList.get(position).getId() + "");
                    }
                }
        );
        adapter.setFeedList(feedList);
        feedRecyclerView.setHasFixedSize(true);
        feedRecyclerView.setAdapter(adapter);
        feedRecyclerView.setLayoutManager(layoutManager);
    }

    public void createFeed () {
        Intent intent = WriteFeedActivity.getIntent(getActivity());
        intent.putExtra("city", feedSpinner.getFirstVisiblePosition() - 1);
        startActivityForResult(intent, REQ_WRITE_FEED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_WRITE_FEED)
            if (resultCode == RESULT_OK) {
                feedSpinner.setSelection(data.getIntExtra("city", 0));

                final ProgressBarDialogUtil progressBarDialogUtil = new ProgressBarDialogUtil(getActivity());
                progressBarDialogUtil.show();
                FeedConnection connection = new FeedConnection(FeedConnection.GET_LIST);
                connection.setListConnectionListener(new ListConnectionListener<FeedVo>() {
                    @Override
                    public void setList(ArrayList<FeedVo> list) {
                        progressBarDialogUtil.cancel();
                        feedList = list;
                        adapter.setFeedList(feedList);
                        adapter.notifyDataSetChanged();
                    }
                });
                connection.execute(feedSpinner.getFirstVisiblePosition() + "");
            }
    }
}
