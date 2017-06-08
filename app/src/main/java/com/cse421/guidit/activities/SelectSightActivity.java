package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.AddSightRecyclerViewAdapter;
import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.connections.FavoriteConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.SightVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectSightActivity extends AppCompatActivity {

    @BindView(R.id.favorite_recycler) RecyclerView favoriteRecyclerView;
    @BindView(R.id.select_sight_confirm)
    TextView confirmBtn;
    @BindView(R.id.tv)
    TextView title;

    private ArrayList<SightVo> sightList;
    private AddSightRecyclerViewAdapter adapter;

    public static Intent getIntent (Context context) {
        return new Intent(context, SelectSightActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sight);

        ButterKnife.bind(this);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");
        confirmBtn.setTypeface(type);
        title.setTypeface(type);

        setViews();
        getFavoriteList();
    }

    private void setViews () {
        sightList = new ArrayList<>();
        adapter = new AddSightRecyclerViewAdapter(this);
        adapter.setList(sightList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favoriteRecyclerView.setHasFixedSize(true);
        favoriteRecyclerView.setAdapter(adapter);
        favoriteRecyclerView.setLayoutManager(layoutManager);
    }

    private void getFavoriteList () {
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();

        FavoriteConnection connection = new FavoriteConnection(FavoriteConnection.Modes.GET_LIST);
        connection.setListConnectionListener(new ListConnectionListener() {
            @Override
            public void setList(ArrayList list) {
                progressBar.cancel();
                sightList = list;
                adapter.setList(sightList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(SelectSightActivity.this, "인터넷 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void listIsEmpty() {
                progressBar.cancel();
                Toast.makeText(SelectSightActivity.this, "찜목록이 비어있습니다", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute();
    }

    @OnClick(R.id.select_sight_confirm)
    public void confirm () {
        ArrayList<SightVo> result = new ArrayList<>();
        for (SightVo sightVo : sightList) {
            if (sightVo.isChecked())
                result.add(sightVo);
        }

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("list", result);
        setResult(RESULT_OK, intent);
        finish();
    }
}
