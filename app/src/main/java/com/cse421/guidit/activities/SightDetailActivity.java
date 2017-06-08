package com.cse421.guidit.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.CommentListRecyclerViewAdapter;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.CommentConnection;
import com.cse421.guidit.connections.SightDetailConnection;
import com.cse421.guidit.fragments.MapFragment;
import com.cse421.guidit.fragments.SightListFragment;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.CommentVo;
import com.cse421.guidit.vo.SightVo;
import com.cse421.guidit.vo.UserVo;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightDetailActivity extends AppCompatActivity {

    public SightVo sightVo;
    UserVo userVo;
    int sightId;
    double myScore;
    boolean isFavorite=false;
    String comment;
    String date;

    @BindView(R.id.sight_image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.phone) TextView phone;
    @BindView(R.id.score) TextView score;
    @BindView(R.id.favorite) ImageView favorite;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.hashtag) TextView hashtag;

    //comment
    @BindView(R.id.wrapper)
    LinearLayout wrapper;
    @BindView(R.id.comment_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.comment)
    EditText commentEditText;
    public ArrayList<CommentVo> commentList;
    CommentListRecyclerViewAdapter adapter;
    private ListConnectionListener listener;

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
                myScore = (double)rating;
                setMyScore();
            }
        });

        commentList = new ArrayList<>();

        loadData();

        //create comment

        //comment
        setRecyclerView();
        loadComment();
    }

    @OnClick(R.id.favorite)
    public void favorite () {
        isFavorite = !isFavorite;
        favorite.setSelected(isFavorite);
        setFavorite();
    }

    @OnClick(R.id.comment_new)
    public void commentCreate(){
        comment = commentEditText.getText().toString();
        long now = System.currentTimeMillis();
        Date tdate = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(tdate);

        setComment();
    }

    public void loadData(){
        SightDetailConnection connection = new SightDetailConnection(1111);
        connection.setActivity(this);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결 ㅇㅋ", Toast.LENGTH_SHORT).show();

                String infos = sightVo.getInformation();
                String[] info = infos.split("%");
                if(info.length == 4){
                    phone.setText(info[1].substring(4,info[1].length()));
                    location.setText(info[3].substring(8,info[3].length()));
                }else if(info.length == 2){
                    phone.setText("등록된 연락처가 없습니다");
                    location.setText(info[1].substring(8,info[1].length()));
                }else{
                    phone.setText("등록된 연락처가 없습니다");
                    location.setText("등록된 주소가 없습니다");
                }

                if (!sightVo.getPicture().equals("null")) {
                    Timber.d("picture : " + sightVo.getPicture());
                    Picasso.with(getApplicationContext())
                            .load(sightVo.getPicture())
                            .into(image);
                }
                title.setText(sightVo.getName());
                score.setText(sightVo.getScore()+"");
                favorite.setSelected(sightVo.isFavorite());
                hashtag.setText("#"+sightVo.getName().replaceAll("\\s",""));
                if(sightVo.getMyScore() == -1){
                    ratingBar.setRating(0);
                }else {
                    ratingBar.setRating((float) sightVo.getMyScore());
                }
                isFavorite = sightVo.isFavorite();
                hashtag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/explore/tags/"+sightVo.getName().replaceAll("\\s","")));
                        startActivity(i);
                    }
                });
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

    public void setMyScore(){
        SightDetailConnection connection = new SightDetailConnection(3333);
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
        connection.execute(userVo.getId()+"",sightId+"",myScore+"");
    }

    public void setComment(){
        CommentConnection connection = new CommentConnection(2222);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결 ㅇㅋ", Toast.LENGTH_SHORT).show();
                commentEditText.setText("");
                loadComment();
                adapter.setList(commentList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void connectionFailed() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(userVo.getId()+"",sightId+"",comment+"",date+"");
    }

    public void loadComment(){
        CommentConnection connection = new CommentConnection(1111);
        connection.setActivity(this);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                setRecyclerView();
            }

            @Override
            public void connectionFailed() {
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();

            }
        });
        connection.execute(sightId+"");
    }

    public void setRecyclerView(){

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

}
