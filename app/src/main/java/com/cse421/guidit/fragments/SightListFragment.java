package com.cse421.guidit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cse421.guidit.R;
import com.cse421.guidit.activities.SightActivity;
import com.cse421.guidit.activities.SightDetailActivity;
import com.cse421.guidit.adapters.SightListRecyclerViewAdapter;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.vo.SightListVo;
import com.cse421.guidit.vo.SightVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightListFragment extends Fragment {

    @BindView(R.id.sightlist_recyclerview)
    RecyclerView sightRecyclerView;

    public ArrayList<SightVo> sightList;
    SightListRecyclerViewAdapter adapter;
    SightListVo sightListVo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sightlist, container, false);
        ButterKnife.bind(this, view);

        sightList = new ArrayList<>();
        initList();

        setRecyclerView();
        sightRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Intent i = new Intent(getActivity(), SightDetailActivity.class);
                startActivity(i);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("--->","onResume called");
//        sightList = ((SightActivity) getActivity()).sightList;
        sightList = sightListVo.getSightList();
        adapter.setList(sightList);
        adapter.notifyDataSetChanged();
    }

    public void setRecyclerView(){

//        test();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Log.e("sightList num",sightList.size()+"");
        adapter = new SightListRecyclerViewAdapter(sightList,
                getContext(),
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        getActivity().startActivity(new Intent(getActivity(), SightDetailActivity.class));
                    }
                }
        );
//        upperAdapter.setList(upperList);
        sightRecyclerView.setHasFixedSize(true);
        sightRecyclerView.setAdapter(adapter);
        sightRecyclerView.setLayoutManager(layoutManager);
    }

    public void initList(){
        sightList.add(new SightVo(1, "fff", "D", "ddd", 3.2, 132.2, 32.2));
    }
}
