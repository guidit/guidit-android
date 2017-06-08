package com.cse421.guidit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.cse421.guidit.R;
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

        setRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        sightList = sightListVo.getInstance().getSightList();
        adapter.setList(sightList);
        adapter.notifyDataSetChanged();
    }

    public void setRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new SightListRecyclerViewAdapter(sightList,
                getApplicationContext(),
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        Intent intent = new Intent(SightListActivity.this, SightDetailActivity.class);
                        intent.putExtra("sightId",sightList.get(position).getId());
                        startActivity(intent);
                    }
                }
        );
        sightRecyclerView.setHasFixedSize(true);
        sightRecyclerView.setAdapter(adapter);
        sightRecyclerView.setLayoutManager(layoutManager);
    }

}
