package com.cse421.guidit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.SightDetailConnection;
import com.cse421.guidit.fragments.MapFragment;
import com.cse421.guidit.fragments.SightListFragment;
import com.cse421.guidit.vo.SightVo;
import com.cse421.guidit.vo.UserVo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightDetailActivity extends AppCompatActivity {

    public SightVo sightVo;
    UserVo userVo;
    int sightId;

    @BindView(R.id.sight_image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.score) TextView score;
    @BindView(R.id.favorite) TextView favorite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        sightId = intent.getExtras().getInt("sightId");
        Log.e("sightId :",sightId+"");

        userVo = UserVo.getInstance();

        loadData();
    }

    public void loadData(){
        SightDetailConnection connection = new SightDetailConnection();
        connection.setActivity(this);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결 ㅇㅋ", Toast.LENGTH_SHORT).show();

                Picasso.with(getApplicationContext())
                        .load(sightVo.getPicture())
                        .into(image);
                title.setText(sightVo.getName());
                location.setText(sightVo.getInformation());
                score.setText(sightVo.getScore()+"");
                favorite.setText(sightVo.isFavorite()+"");
            }

            @Override
            public void connectionFailed() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(userVo.getId()+"",sightId+"");
    }
}
