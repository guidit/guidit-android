package com.cse421.guidit.fragments;

import android.content.Intent;
import android.graphics.Typeface;
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
import com.cse421.guidit.activities.FavoriteActivity;
import com.cse421.guidit.activities.OtherPlanActivity;
import com.cse421.guidit.activities.PlanActivity;
import com.cse421.guidit.activities.UserSettingActivity;
import com.cse421.guidit.adapters.MyPagePlanRecyclerAdapter;
import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.connections.PlanConnection;
import com.cse421.guidit.util.CircleTransform;
import com.cse421.guidit.util.ProgressBarDialogUtil;
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

    public final int REQUEST_ADD_PLAN = 1357;
    Typeface type;
    
    @BindView(R.id.my_profile_img) ImageView profileImage;
    @BindView(R.id.my_name) TextView userName;
    @BindView(R.id.my_plan_list) RecyclerView myPlanRecyclerView;
    @BindView(R.id.plan) TextView plan;
    @BindView(R.id.favorite) TextView favorite;
    
    private ArrayList<PlanVo> planList;
    private MyPagePlanRecyclerAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        ButterKnife.bind(this, view);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/BMJUA_ttf.ttf");
        plan.setTypeface(type);
        favorite.setTypeface(type);

        setViews();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        UserVo userVo = UserVo.getInstance();
    
        // 프로필
        if (!userVo.getProfile().equals("null"))
            Picasso.with(getActivity())
                    .load(userVo.getProfile())
                    .resize(800, 600)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(profileImage);
    
        userName.setText(userVo.getName());
        userName.setTypeface(type);
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
                        Intent intent = PlanActivity.getIntent(getActivity());
                        intent.putExtra("planId", planList.get(position).getId());
                        startActivity(intent);
                    }
                }
        );
        adapter.setPlanList(planList);
        myPlanRecyclerView.setHasFixedSize(true);
        myPlanRecyclerView.setAdapter(adapter);
        myPlanRecyclerView.setLayoutManager(layoutManager);

        //리스트 불러오기
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(getActivity());
        progressBar.show();

        PlanConnection connection = new PlanConnection(PlanConnection.modes.MY_PLANS);
        connection.setListConnectionListener(new ListConnectionListener<PlanVo>() {
            @Override
            public void setList(ArrayList<PlanVo> list) {
                progressBar.cancel();
                planList = list;
                adapter.setPlanList(planList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void listIsEmpty() {
                progressBar.cancel();
                Toast.makeText(getActivity(), "여행 계획이 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute();
    }

    public void addPlan () {
        startActivityForResult(PlanActivity.getIntent(getActivity()), REQUEST_ADD_PLAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_PLAN)
            if (resultCode == RESULT_OK) {
                final ProgressBarDialogUtil progresbar = new ProgressBarDialogUtil(getActivity());
                progresbar.show();

                PlanConnection connection = new PlanConnection(PlanConnection.modes.MY_PLANS);
                connection.setListConnectionListener(new ListConnectionListener<PlanVo>() {
                    @Override
                    public void setList(ArrayList<PlanVo> list) {
                        progresbar.cancel();
                        planList = list;
                        adapter.setPlanList(planList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void connectionFailed() {
                        progresbar.cancel();
                        Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void listIsEmpty() {
                        progresbar.cancel();
                        Toast.makeText(getActivity(), "여행 계획이 없습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                connection.execute();
            }
    }

    @OnClick(R.id.user_setting)
    public void setting () {
        startActivity(UserSettingActivity.getIntent(getActivity()));
    }
    
    @OnClick(R.id.plan)
    public void plan () {
        startActivity(OtherPlanActivity.getIntent(getActivity()));
    }
    
    @OnClick(R.id.favorite)
    public void favorite () {
        startActivity(FavoriteActivity.getIntent(getActivity()));
    }
}
