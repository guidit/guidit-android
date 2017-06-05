package com.cse421.guidit.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.CommentListRecyclerViewAdapter;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.SightDetailConnection;
import com.cse421.guidit.fragments.MapFragment;
import com.cse421.guidit.fragments.SightListFragment;
import com.cse421.guidit.vo.CommentVo;
import com.cse421.guidit.vo.SightVo;
import com.cse421.guidit.vo.UserVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightDetailActivity extends AppCompatActivity {

    public SightVo sightVo;
    UserVo userVo;
    int sightId;
    boolean isFavorite=false;

    @BindView(R.id.sight_image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.score) TextView score;
    @BindView(R.id.favorite) ImageView favorite;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    //comment
    @BindView(R.id.wrapper)
    LinearLayout wrapper;
    @BindView(R.id.comment_recyclerview)
    RecyclerView recyclerView;
    ArrayList<CommentVo> commentList;
    CommentListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight_detail);
        ButterKnife.bind(this);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");
        title.setTypeface(type);

        Intent intent = getIntent();
        sightId = intent.getExtras().getInt("sightId");
        Log.e("sightId :",sightId+"");

        userVo = UserVo.getInstance();

        //rating bar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("rating --->",rating+"");
            }
        });

        //comment
        commentList = new ArrayList<>();
        loadData();
        setRecyclerView();
    }

    @OnClick(R.id.favorite)
    public void favorite () {
        isFavorite = !isFavorite;
        favorite.setSelected(isFavorite);
        setFavorite();
    }

    public void loadData(){
        SightDetailConnection connection = new SightDetailConnection(1111);
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
                favorite.setSelected(sightVo.isFavorite());

                isFavorite = sightVo.isFavorite();
            }

            @Override
            public void connectionFailed() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(userVo.getId()+"",sightId+"");
    }

    public void setFavorite(){
        Log.e("is Favoritee",isFavorite+"");
        SightDetailConnection connection = new SightDetailConnection(2222);
        connection.setActivity(this);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결 ㅇㅋ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectionFailed() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(userVo.getId()+"",sightId+"",isFavorite+"");
    }

    public void setRecyclerView(){

        test();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Log.e("sightList num",commentList.size()+"");
        adapter = new CommentListRecyclerViewAdapter(commentList,
                getApplicationContext());
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        ViewGroup.LayoutParams params = wrapper.getLayoutParams();
        float scale = getResources().getDisplayMetrics().density;
        params.height = (int)scale*105*commentList.size();
        wrapper.setLayoutParams(params);

        recyclerView.clearFocus();
    }

    public void test(){
        commentList.add(new CommentVo(1,"http://www.9dog.co.kr/wp-content/uploads/2013/07/img_0214.jpg","julie90555","ㅎㅎㅎㅎㅎㅎ안드로이드 야야야얍","2017.18.29"));
        commentList.add(new CommentVo(2,"http://www.9dog.co.kr/wp-content/uploads/2013/07/img_0214.jpg","julie9055","ㅎㅎㅎㅎ안드로이드 야야야얍","2017.18.29"));
        commentList.add(new CommentVo(3,"http://www.9dog.co.kr/wp-content/uploads/2013/07/img_0214.jpg","julie905","ㅎㅎㅎ안드로이드 야야야얍","2017.18.29"));
    }

}
