package com.cse421.guidit.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.adapters.SightListRecyclerViewAdapter;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.fragments.MapFragment;
import com.cse421.guidit.fragments.SightListFragment;
import com.cse421.guidit.vo.SightListVo;
import com.cse421.guidit.vo.SightVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightListActivity extends AppCompatActivity {

    @BindView(R.id.sightlist_recyclerview)
    RecyclerView sightRecyclerView;

    public ArrayList<SightVo> sightList;
    SightListRecyclerViewAdapter adapter;
    SightListVo sightListVo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightlist);

        ButterKnife.bind(this);

        sightList = new ArrayList<>();
        sightListVo = SightListVo.getInstance();

        initList();
        setRecyclerView();
        sightRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Intent i = new Intent(SightListActivity.this, SightDetailActivity.class);
                startActivity(i);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("--->","onResume called");
//        sightList = ((SightActivity) getActivity()).sightList;
        sightList = sightListVo.getInstance().getSightList();
        Log.d("--->",sightList.size()+"");
        adapter.setList(sightList);
        adapter.notifyDataSetChanged();
    }

    public void setRecyclerView(){

//        test();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Log.e("sightList num",sightList.size()+"");
        adapter = new SightListRecyclerViewAdapter(sightList,
                getApplicationContext(),
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        startActivity(new Intent(SightListActivity.this, SightDetailActivity.class));
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
