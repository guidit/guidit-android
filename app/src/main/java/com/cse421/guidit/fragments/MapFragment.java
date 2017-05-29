package com.cse421.guidit.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.activities.SightActivity;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.SightConnection;
import com.cse421.guidit.navermap.NMapPOIflagType;
import com.cse421.guidit.navermap.NMapViewerResourceProvider;
import com.cse421.guidit.vo.SightVo;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

import static android.view.View.X;
import static android.view.View.Y;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class MapFragment extends Fragment {

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            basicX = bundle.getDouble("basicX");
            basicY = bundle.getDouble("basicY");
        }
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NMapView mapView = (NMapView)getView().findViewById(R.id.daum_map);
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mMapContext.setupMapView(mapView);

        // mapview setting
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getContext());

        // create overlay manager
        mOverlayManager = new NMapOverlayManager(getContext(), mapView, mMapViewerResourceProvider);

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
        connection.setFragment(this);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                //progressBar.cancel();
                Toast.makeText(getActivity(), "인터넷 연결 ㅇㅋ", Toast.LENGTH_SHORT).show();

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
            }

            @Override
            public void connectionFailed() {
                //progressBar.cancel();
                Toast.makeText(getActivity(), "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(basicX+"",basicY+"");
    }

    @Override
    public void onStart(){
        super.onStart();
        mMapContext.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }
    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }
}
