package com.cse421.guidit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cse421.guidit.R;
import com.cse421.guidit.navermap.NMapPOIflagType;
import com.cse421.guidit.navermap.NMapViewerResourceProvider;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import butterknife.ButterKnife;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sightlist, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
