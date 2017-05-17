package com.cse421.guidit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.fragments.MapFragment;
import com.cse421.guidit.fragments.SightListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;

    //Fragment
    private MapFragment mapFragment;
    private SightListFragment sightListFragment;

    // Adapter
    private MainPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);

        ButterKnife.bind(this);

        setViews();
    }

    private void setViews () {
        mapFragment = new MapFragment();
        sightListFragment = new SightListFragment();

        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(mapFragment, "지도로 보기");
        pagerAdapter.addFragment(sightListFragment, "목록으로 보기");


        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // connect tabs and viewpager
        tabLayout.setupWithViewPager(viewPager);
    }
}
