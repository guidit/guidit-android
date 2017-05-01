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

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.fragments.BrowseFragment;
import com.cse421.guidit.fragments.FeedFragment;
import com.cse421.guidit.fragments.MyPageFragment;
import com.cse421.guidit.fragments.SightFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
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

    public static Intent getIntent (Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // set up fragments and view pager
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
