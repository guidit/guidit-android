package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.FavoriteRecyclerViewAdapter;
import com.cse421.guidit.callbacks.FavoriteClickEventListener;
import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.FavoriteConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.SightVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.favorite_recycler) RecyclerView recyclerView;

    private ArrayList<SightVo> favorites;
    private FavoriteRecyclerViewAdapter adapter;

    public static Intent getIntent (Context context) {
        return new Intent(context, FavoriteActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        setToolbar();
        setRecycler();
        getFavoriteList();
    }

    private void setToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setRecycler () {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        favorites = new ArrayList<>();
        adapter = new FavoriteRecyclerViewAdapter(this, favorites);
        adapter.setListener(new FavoriteClickEventListener() {
            @Override
            public void itemClicked(int position) {
                //// TODO: 2017-05-21 정이 관광지 상세정보 액티비티?
            }

            @Override
            public void itemDeleted(int position) {
                delete(position);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }

    private void getFavoriteList () {
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();

        FavoriteConnection connection = new FavoriteConnection(FavoriteConnection.Modes.GET_LIST);
        connection.setListConnectionListener(new ListConnectionListener() {
            @Override
            public void setList(ArrayList list) {
                progressBar.cancel();
                favorites = list;
                adapter.setList(favorites);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(FavoriteActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute();
    }

    private void delete (final int position) {
        //// TODO: 2017-05-21 지우기전에 다이얼로그로 물어볼까?
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();

        FavoriteConnection connection = new FavoriteConnection(FavoriteConnection.Modes.DELETE);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                progressBar.cancel();
                favorites.remove(position);
                adapter.setList(favorites);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(FavoriteActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
            default:
                Toast.makeText(this, "잘못된 입력입니다", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
