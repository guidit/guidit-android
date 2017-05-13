package com.cse421.guidit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.cse421.guidit.R;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;


import butterknife.ButterKnife;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class MapActivity  extends NMapActivity {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "mX_lrVWKTUXNDb5evrDV";// 애플리케이션 클라이언트 아이디 값

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = new NMapView(this);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정

        ViewGroup viewGroup = (ViewGroup)findViewById(R.id.daum_map);
        viewGroup.addView(mMapView);

        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
    }
}
