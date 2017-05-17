package com.cse421.guidit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.LocationRecyclerViewAdapter;
import com.cse421.guidit.adapters.SightListRecyclerViewAdapter;
import com.cse421.guidit.navermap.NMapPOIflagType;
import com.cse421.guidit.navermap.NMapViewerResourceProvider;
import com.cse421.guidit.vo.SightListVo;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightListFragment extends Fragment {

    @BindView(R.id.sightlist_recyclerview)
    RecyclerView sightRecyclerView;

    private SightListRecyclerViewAdapter sightAdapter;
    ArrayList<SightListVo> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sightlist, container, false);
        ButterKnife.bind(this, view);

        setRecyclerView();

        return view;
    }

    public void setRecyclerView(){

        test();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SightListRecyclerViewAdapter adapter = new SightListRecyclerViewAdapter(items, getContext());
//        upperAdapter.setList(upperList);
        sightRecyclerView.setHasFixedSize(true);
        sightRecyclerView.setAdapter(adapter);
        sightRecyclerView.setLayoutManager(layoutManager);
    }

    public void test(){
        items = new ArrayList<>();
        items.add(new SightListVo(R.drawable.logo,"a","aaa"));
        items.add(new SightListVo(R.drawable.logo,"b","aaa"));
        items.add(new SightListVo(R.drawable.logo,"c","aaa"));
        items.add(new SightListVo(R.drawable.logo,"d","aaa"));
    }
}
