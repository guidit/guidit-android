package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.MainConnection;
import com.cse421.guidit.fragments.BrowseFragment;
import com.cse421.guidit.fragments.FeedFragment;
import com.cse421.guidit.fragments.MyPageFragment;
import com.cse421.guidit.fragments.SightFragment;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.FeedVo;
import com.cse421.guidit.vo.PlanVo;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.main_fab) FloatingActionButton addButton;

    // Fragment
    private BrowseFragment browseFragment;
    private SightFragment sightFragment;
    private FeedFragment feedFragment;
    private MyPageFragment myPageFragment;

    // Adapter
    private MainPagerAdapter pagerAdapter;
    
    // Dialog
    private ProgressBarDialogUtil progressBar;

    // 각 탭별 정보
    public PlanVo hotPlan;
    public SightVo hotSight;
    public ArrayList<FeedVo> feedList;
    public ArrayList<PlanVo> myPlanList;

    public static Intent getIntent (Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // 서버 통신 전까지 프로그래스 바만 돌고있는다
        progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();
    
        MainConnection connection = new MainConnection();
        connection.setActivity(this);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                progressBar.cancel();
    
                // 통신 완료 후 탭별 정보 객체에 데이터가 들어오면
                //setViews();
            }
    
            @Override
            public void connectionFailed() {
                progressBar.cancel();
    
                Toast.makeText(MainActivity.this, "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
        //connection.execute();
        //// TODO: 2017. 5. 9. test 용
        test();
    }
    
    private void test () {
        progressBar.cancel();
        
        hotPlan = new PlanVo();
        hotPlan.setId(3);
        hotPlan.setName("나는 여행을 한다");
        hotPlan.setViewCount(20);
        hotPlan.setPublic(true);
        
        hotSight = new SightVo();
        hotSight.setId(5);
        hotSight.setName("십리대밭");
        hotSight.setPicture("http://cfile22.uf.tistory.com/image/2341FC4A51B5E60338157B");
        hotSight.setInformation("태화강변에 있는 대밭이다.");
        hotSight.setScore(3.7);
        
        myPlanList = new ArrayList<>();
        PlanVo plan1 = new PlanVo();
        plan1.setName("첫번째 여행");
        plan1.setPublic(true);
        PlanVo plan2 = new PlanVo();
        plan2.setName("두번째 여행");
        plan2.setPublic(false);
        PlanVo plan3 = new PlanVo();
        plan3.setName("세번째 여행");
        plan3.setPublic(true);
        myPlanList.add(plan1);
        myPlanList.add(plan2);
        myPlanList.add(plan3);

        feedList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FeedVo feedVo = new FeedVo();
            feedVo.setId(i);
            feedVo.setProfile("testprofile");
            feedVo.setCity("울산");
            feedVo.setContent("내용내용" + i);
            feedVo.setUserName("유저이름" + i);
            feedVo.setDate("17년 5월 31일");
            feedVo.setUserId(i + "" + i);
            feedList.add(feedVo);
        }
        FeedVo feedVo = new FeedVo();
        feedVo.setId(5);
        feedVo.setProfile("testprofile");
        feedVo.setCity("울산");
        feedVo.setContent("내용내용");
        feedVo.setUserName("유저이름");
        feedVo.setDate("17년 5월 31일");
        feedVo.setUserId("ho");
        feedList.add(feedVo);

        setViews();
    }

    private void setViews () {
        browseFragment = new BrowseFragment();
        sightFragment = new SightFragment();
        feedFragment = new FeedFragment();
        myPageFragment = new MyPageFragment();

        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(browseFragment, "둘러보기");
        pagerAdapter.addFragment(sightFragment, "여행정보");
        pagerAdapter.addFragment(feedFragment, "지역피드");
        pagerAdapter.addFragment(myPageFragment, "나의여행");

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 2:
                        Timber.d("page selected " + 2);
                        addButton.setImageResource(R.drawable.ic_create);
                        addButton.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        Timber.d("page selected " + 3);
                        addButton.setImageResource(R.drawable.ic_add);
                        addButton.setVisibility(View.VISIBLE);
                        break;
                    default:
                        addButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // connect tabs and viewpager
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.main_fab)
    public void addPlan(View view) {
        switch (viewPager.getCurrentItem()) {
            case 2:
                feedFragment.createFeed();
                break;
            case 3:
                myPageFragment.addPlan();
                break;
            default:
                Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_SHORT).show();
        }
    }
}
