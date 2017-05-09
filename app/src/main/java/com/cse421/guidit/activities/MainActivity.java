package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.callbacks.SimpleEventListener;
import com.cse421.guidit.connections.MainConnection;
import com.cse421.guidit.fragments.BrowseFragment;
import com.cse421.guidit.fragments.FeedFragment;
import com.cse421.guidit.fragments.MyPageFragment;
import com.cse421.guidit.fragments.SightFragment;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.FeedVo;
import com.cse421.guidit.vo.FestivalVo;
import com.cse421.guidit.vo.PlanVo;
import com.cse421.guidit.vo.SightVo;
import com.cse421.guidit.vo.UserVo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.my_page_fab) FloatingActionButton addPlanBtn;

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
        connection.setListener(new SimpleEventListener() {
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
        
        //// TODO: 2017. 5. 9. 나머지 탭의 정보도 넣어야함
        
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
                //// TODO: 2017. 5. 1. 나의 여행 탭 클릭시 fab 활성화
                if (position == 3) {
                    addPlanBtn.setVisibility(View.VISIBLE);
                } else {
                    addPlanBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // connect tabs and viewpager
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.my_page_fab)
    public void addPlan(View view) {
        //// TODO: 2017. 5. 1. 여행계획 추가 버튼
    }
}
