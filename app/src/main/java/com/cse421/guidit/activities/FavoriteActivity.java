package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    @BindView(R.id.favorite_recycler) RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;

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

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");
        title.setTypeface(type);

        setRecycler();
        getFavoriteList();
    }

    private void setRecycler () {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        favorites = new ArrayList<>();
        adapter = new FavoriteRecyclerViewAdapter(this, favorites);
        adapter.setListener(new FavoriteClickEventListener() {
            @Override
            public void itemClicked(int position) {
                Intent intent = new Intent(FavoriteActivity.this, SightDetailActivity.class);
                intent.putExtra("sightId", favorites.get(position).getId());
                startActivity(intent);
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

            @Override
            public void listIsEmpty() {
                progressBar.cancel();
                Toast.makeText(FavoriteActivity.this, "찜목록이 비어있습니다", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute();
    }

    private void delete (final int position) {
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
        connection.execute(favorites.get(position).getId() + "");
    }
}
