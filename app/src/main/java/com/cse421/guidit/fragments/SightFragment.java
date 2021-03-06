package com.cse421.guidit.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.activities.SightActivity;
import com.cse421.guidit.adapters.LocationRecyclerViewAdapter;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class SightFragment extends Fragment {
    
    @BindView(R.id.upper_locations) RecyclerView upperRecyclerView;
    @BindView(R.id.lower_locations) RecyclerView lowerRecyclerView;

    @BindView(R.id.wheretogo)
    TextView title;

    @BindArray(R.array.upper_locations) String [] upperLocations;
    @BindArray(R.array.lower_seoul) String [] seoul;
    @BindArray(R.array.lower_kyunki) String [] kyunki;
    @BindArray(R.array.lower_kangwon) String [] kangwon;
    @BindArray(R.array.lower_choongchung) String [] choongchung;
    @BindArray(R.array.lower_kyeongsang) String [] kyeongsang;
    @BindArray(R.array.lower_busan) String [] busan;
    @BindArray(R.array.lower_junra) String [] junra;
    @BindArray(R.array.lower_jeju) String [] jeju;
    
    private ArrayList<String> upperList;
    private ArrayList<String> [] lowerList;
    private LocationRecyclerViewAdapter upperAdapter;
    private LocationRecyclerViewAdapter lowerAdapter;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sight, container, false);
        ButterKnife.bind(this, view);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/BMJUA_ttf.ttf");
        title.setTypeface(type);

        convertXMLToArrayList();
        setUpperRecyclerView();
        setLowerRecyclerView();

        return view;
    }
    
    private void convertXMLToArrayList () {
        upperList = new ArrayList<>();
        for (String s : upperLocations)
            upperList.add(s);
        
        lowerList = new ArrayList[9];
        for (int i = 0; i < lowerList.length; i++)
            lowerList[i] = new ArrayList<>();
        
        for (String s : seoul)
            lowerList[0].add(s);
        for (String s : kyunki)
            lowerList[1].add(s);
        for (String s : kangwon)
            lowerList[2].add(s);
        for (String s : choongchung)
            lowerList[3].add(s);
        for (String s : kyeongsang)
            lowerList[4].add(s);
        for (String s : busan)
            lowerList[5].add(s);
        for (String s : junra)
            lowerList[6].add(s);
        for (String s : jeju)
            lowerList[7].add(s);
    }
    
    private void setUpperRecyclerView () {
        LinearLayoutManager upperLayoutManager = new LinearLayoutManager(getActivity());
        upperLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    
        upperAdapter = new LocationRecyclerViewAdapter(
                getActivity(),
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        changeLowerLocations(position);
                    }
                }
        );
        upperAdapter.setList(upperList);
        upperRecyclerView.setHasFixedSize(true);
        upperRecyclerView.setAdapter(upperAdapter);
        upperRecyclerView.setLayoutManager(upperLayoutManager);
    }
    
    private void setLowerRecyclerView () {
        LinearLayoutManager lowerLayoutManager = new LinearLayoutManager(getActivity());
        lowerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    
        lowerAdapter = new LocationRecyclerViewAdapter(
                getActivity(),
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        // lowerAdapter.getList().getposition = 원주, 평창, 강릉 등 세부적인 지역명
                        Intent i = new Intent(getActivity(), SightActivity.class);
                        i.putExtra("basicCity",lowerAdapter.getList().get(position));
                        getActivity().startActivity(i);
                        Toast.makeText(getActivity(), lowerAdapter.getList().get(position), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        lowerAdapter.setList(lowerList[0]);
        lowerRecyclerView.setHasFixedSize(true);
        lowerRecyclerView.setAdapter(lowerAdapter);
        lowerRecyclerView.setLayoutManager(lowerLayoutManager);
    }
    
    private void changeLowerLocations (int position) {
        lowerAdapter.setList(lowerList[position]);
        lowerAdapter.customDataSetChange();
    }
}
