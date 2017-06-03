package com.cse421.guidit.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.MainPagerAdapter;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.SightConnection;
import com.cse421.guidit.fragments.MapFragment;
import com.cse421.guidit.fragments.SightListFragment;
import com.cse421.guidit.navermap.NMapViewerResourceProvider;
import com.cse421.guidit.vo.SightListVo;
import com.cse421.guidit.vo.SightVo;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.System.in;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class MapActivity extends NMapActivity {

    //map
    private NMapContext mMapContext;// 지도 화면 View
    private final String CLIENT_ID = "mX_lrVWKTUXNDb5evrDV";// 애플리케이션 클라이언트 아이디 값

    NMapViewerResourceProvider mMapViewerResourceProvider;
    NMapPOIdata poiData;
    NMapOverlayManager mOverlayManager;
    private NMapController mMapController;

    //data
    Double basicX;
    Double basicY;
    public ArrayList<SightVo> sightList;
    SightListVo sightListVo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);

        sightListVo = new SightListVo();
        mMapContext = new NMapContext(this);
        mMapContext.onCreate();

        //XY 값 받아오기
        Intent i = getIntent();
        basicX = i.getExtras().getDouble("basicX");
        basicY = i.getExtras().getDouble("basicY");


        //map setting
        NMapView mapView = (NMapView)findViewById(R.id.daum_map);
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mMapContext.setupMapView(mapView);

        // mapview setting
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getApplicationContext());

        // create overlay manager
        mOverlayManager = new NMapOverlayManager(getApplicationContext(), mapView, mMapViewerResourceProvider);

        //connection - sightList 받아오기
        loadData();

        //center - basic
        mMapController = mapView.getMapController();
        mMapController.setMapCenter(new NGeoPoint(basicX,basicY));

        //센터 바뀌면 데이터 다시 로드
        mapView.setOnMapStateChangeListener(new NMapView.OnMapStateChangeListener() {
            @Override
            public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {

            }

            @Override
            public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {
                basicX = nGeoPoint.getLongitude();
                basicY = nGeoPoint.getLatitude();
                Log.e("--->","basicX:"+basicX+" basicY:"+basicY);
                loadData();
            }

            @Override
            public void onMapCenterChangeFine(NMapView nMapView) {

            }

            @Override
            public void onZoomLevelChange(NMapView nMapView, int i) {

            }

            @Override
            public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

            }
        });
    }

    public void loadData(){
        SightConnection connection = new SightConnection();
        connection.setMapActivity(this);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결 ㅇㅋ", Toast.LENGTH_SHORT).show();

                //좌표뿌리기
                // set POI data
                poiData = new NMapPOIdata(sightList.size(), mMapViewerResourceProvider);
                Drawable pin;
                poiData.beginPOIdata(sightList.size());
                for (int i=0; i<sightList.size(); i++){
                    switch(sightList.get(i).getType()){
                        case "A":
                            pin = getResources().getDrawable(R.drawable.pin_play);
                            break;
                        case "B":
                            pin = getResources().getDrawable(R.drawable.pin_stay);
                            break;
                        case "C":
                            pin = getResources().getDrawable(R.drawable.pin_eat);
                            break;
                        default:
                            pin = getResources().getDrawable(R.drawable.pin_none);
                            break;
                    }
                    poiData.addPOIitem(sightList.get(i).getMapX(), sightList.get(i).getMapY(), sightList.get(i).getName(),pin,0);
                }
                poiData.endPOIdata();

                // create POI data overlay
                NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                poiDataOverlay.setOnStateChangeListener(new NMapPOIdataOverlay.OnStateChangeListener() {
                    @Override
                    public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                        //마커 선택시
//                        if( nMapPOIitem != null) {
//                            for (int i = 0; i < sightList.size(); i++) {
//                                if (sightList.get(i).getName() == nMapPOIitem.getTitle()) {
//                                    Log.e("->", sightList.get(i).getName() + "");
//                                }
//                            }
//                        }
                    }

                    @Override
                    public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                        //말풍선 선택시
                            Log.e("---->","callout changed");
                            Log.e("->", nMapPOIitem.getTitle() + " ht "+nMapPOIitem.getHeadText() + " sn "+nMapPOIitem.getSnippet() + "");
                            if( nMapPOIitem != null) {
                                for (int i = 0; i < sightList.size(); i++) {
                                    if (sightList.get(i).getName() == nMapPOIitem.getTitle()) {
                                        Log.e("->", sightList.get(i).getName() + "");
                                        Intent intent = new Intent(MapActivity.this, SightDetailActivity.class);
                                        intent.putExtra("sightId",sightList.get(i).getId());
                                        startActivity(intent);
                                    }
                                }
                            }
                    }
                });

                //((SightActivity) getApplicationContext()).sightList = sightList;
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("sightList",sightList);
//                listFragment.setArguments(bundle);
                sightListVo.getInstance().setSightList(sightList);
            }

            @Override
            public void connectionFailed() {
                //progressBar.cancel();
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(basicX+"",basicY+"");
    }
}
