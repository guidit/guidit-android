package com.cse421.guidit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.activities.MainActivity;
import com.cse421.guidit.adapters.PlanRecyclerAdapter;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.util.CircleTransform;
import com.cse421.guidit.vo.PlanVo;
import com.cse421.guidit.vo.UserVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class MyPageFragment extends Fragment {
    
    @BindView(R.id.my_profile_img) ImageView profileImage;
    @BindView(R.id.my_name) TextView userName;
    @BindView(R.id.my_plan_list) RecyclerView myPlanRecyclerView;
    
    private ArrayList<PlanVo> planList;
    private PlanRecyclerAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        ButterKnife.bind(this, view);

        setViews();
        
        return view;
    }
    
    private void setViews () {
        UserVo userVo = UserVo.getInstance();
        
        // 프로필
        if (userVo.getProfile() != null && !userVo.getProfile().equals(""))
            Picasso.with(getActivity())
                    .load(userVo.getProfile())
                    .resize(800, 600)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(profileImage);
        
        userName.setText(userVo.getName());
        
        // 여행계획 리스트
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        
        planList = ((MainActivity) getActivity()).myPlanList;
        adapter = new PlanRecyclerAdapter(
                getActivity(),
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        //// TODO: 2017. 5. 10. 세부 계획으로
                        Toast.makeText(getActivity(), planList.get(position) + "", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        adapter.setPlanList(planList);
        myPlanRecyclerView.setHasFixedSize(true);
        myPlanRecyclerView.setAdapter(adapter);
        myPlanRecyclerView.setLayoutManager(layoutManager);
    }
    
    @OnClick(R.id.user_setting)
    public void setting () {
        //// TODO: 2017. 5. 10. 유저 설정 액티비티로
    }
    
    @OnClick(R.id.plan)
    public void plan () {
        //// TODO: 2017. 5. 10. ?? 얜 뭐하는애지
    }
    
    @OnClick(R.id.favorite)
    public void favorite () {
        //// TODO: 2017. 5. 10. 찜목록 보여주는 액티비티로
    }
}
