package com.cse421.guidit.fragments;

import android.content.Intent;
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
import com.cse421.guidit.activities.PlanActivity;
import com.cse421.guidit.activities.UserSettingActivity;
import com.cse421.guidit.adapters.MyPagePlanRecyclerAdapter;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.util.CircleTransform;
import com.cse421.guidit.vo.PlanVo;
import com.cse421.guidit.vo.UserVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class MyPageFragment extends Fragment {

    public final int REQEST_ADD_PLAN = 1357;
    
    @BindView(R.id.my_profile_img) ImageView profileImage;
    @BindView(R.id.my_name) TextView userName;
    @BindView(R.id.my_plan_list) RecyclerView myPlanRecyclerView;
    
    private ArrayList<PlanVo> planList;
    private MyPagePlanRecyclerAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        ButterKnife.bind(this, view);

        setViews();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        UserVo userVo = UserVo.getInstance();
    
        // 프로필
        if (!userVo.getProfile().equals("testprofile"))
            Picasso.with(getActivity())
                    .load(userVo.getProfile())
                    .resize(800, 600)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(profileImage);
    
        userName.setText(userVo.getName());
    }
    
    private void setViews () {
        // 여행계획 리스트
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        
        planList = new ArrayList<>();
        adapter = new MyPagePlanRecyclerAdapter(
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

        //리스트 불러오기
        //// TODO: 2017-06-04 myplan connection 추가

        PlanVo plan1 = new PlanVo();
        plan1.setName("첫번째 여행");
        plan1.setPublic(true);
        PlanVo plan2 = new PlanVo();
        plan2.setName("두번째 여행");
        plan2.setPublic(false);
        PlanVo plan3 = new PlanVo();
        plan3.setName("세번째 여행");
        plan3.setPublic(true);
        planList.add(plan1);
        planList.add(plan2);
        planList.add(plan3);
        adapter.setPlanList(planList);
        adapter.notifyDataSetChanged();
    }

    public void addPlan () {
        startActivityForResult(PlanActivity.getIntent(getActivity()), REQEST_ADD_PLAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQEST_ADD_PLAN)
            if (resultCode == RESULT_OK) {
                //// TODO: 2017-06-03 사용자 여행계획 새로고침
            }
    }

    @OnClick(R.id.user_setting)
    public void setting () {
        startActivity(UserSettingActivity.getIntent(getActivity()));
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
