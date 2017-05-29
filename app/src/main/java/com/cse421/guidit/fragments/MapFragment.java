package com.cse421.guidit.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.navermap.NMapPOIflagType;
import com.cse421.guidit.navermap.NMapViewerResourceProvider;
import com.cse421.guidit.vo.SightVo;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class MapFragment extends Fragment {

    private NMapContext mMapContext;// 지도 화면 View
    private final String CLIENT_ID = "mX_lrVWKTUXNDb5evrDV";// 애플리케이션 클라이언트 아이디 값

    Double basicX;
    Double basicY;
    ArrayList<SightVo> sightList;

    private NMapController mMapController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            basicX = bundle.getDouble("basicX");
            basicY = bundle.getDouble("basicY");
            sightList = bundle.getParcelableArrayList("sightList");
        }
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();

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
        NMapViewerResourceProvider mMapViewerResourceProvider = new NMapViewerResourceProvider(getContext());

        // create overlay manager
        NMapOverlayManager mOverlayManager = new NMapOverlayManager(getContext(), mapView, mMapViewerResourceProvider);

        int markerId = NMapPOIflagType.PIN;

        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(sightList.size(), mMapViewerResourceProvider);
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

        //center
        mMapController = mapView.getMapController();
        mMapController.setMapCenter(new NGeoPoint(basicX,basicY));


        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
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
