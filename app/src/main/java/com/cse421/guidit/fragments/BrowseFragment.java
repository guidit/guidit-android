package com.cse421.guidit.fragments;

import butterknife.BindView;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.activities.FestivalActivity;
import com.cse421.guidit.activities.MainActivity;
import com.cse421.guidit.activities.PlanActivity;
import com.cse421.guidit.activities.SightDetailActivity;
import com.cse421.guidit.util.ImageUtil;
import com.cse421.guidit.vo.PlanVo;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class BrowseFragment extends Fragment {
    
    @BindView(R.id.festival_1_image) ImageView festival1Image;
    @BindView(R.id.festival_1_month) TextView festival1Month;
    @BindView(R.id.festival_2_image) ImageView festival2Image;
    @BindView(R.id.festival_2_month) TextView festival2Month;
    @BindView(R.id.festival_3_image) ImageView festival3Image;
    @BindView(R.id.festival_3_month) TextView festival3Month;
    @BindView(R.id.festival_4_image) ImageView festival4Image;
    @BindView(R.id.festival_4_month) TextView festival4Month;
    @BindView(R.id.festival_5_image) ImageView festival5Image;
    @BindView(R.id.festival_5_month) TextView festival5Month;
    @BindView(R.id.hot_plan_name) TextView hotPlanName;
    @BindView(R.id.hot_plan_picture) ImageView hotPlanPicture;
    @BindView(R.id.hot_sight_name) TextView hotSightName;
    @BindView(R.id.hot_sight_picture) ImageView hotSightPicture;

    private int currentMonth;
    private PlanVo hotPlan;
    private SightVo hotSight;

    @BindView(R.id.subtitle1) TextView subtitle1;
    @BindView(R.id.subtitle2) TextView subtitle2;
    @BindView(R.id.subtitle3) TextView subtitle3;

    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        ButterKnife.bind(this, view);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/BMJUA_ttf.ttf");
        subtitle1.setTypeface(type);
        subtitle2.setTypeface(type);
        subtitle3.setTypeface(type);

        setViews();

        return view;
    }
    
    private void setViews () {
        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        festival1Month.setText(currentMonth + "월");
        Picasso.with(getActivity())
                .load(ImageUtil.getFestivalImage(currentMonth))
                .into(festival1Image);
        festival2Month.setText(currentMonth + 1 + "월");
        Picasso.with(getActivity())
                .load(ImageUtil.getFestivalImage(currentMonth + 1))
                .into(festival2Image);
        festival3Month.setText(currentMonth + 2+ "월");
        Picasso.with(getActivity())
                .load(ImageUtil.getFestivalImage(currentMonth + 2))
                .into(festival3Image);
        festival4Month.setText(currentMonth + 3 + "월");
        Picasso.with(getActivity())
                .load(ImageUtil.getFestivalImage(currentMonth + 3))
                .into(festival4Image);
        festival5Month.setText(currentMonth + 4 + "월");
        Picasso.with(getActivity())
                .load(ImageUtil.getFestivalImage(currentMonth + 4))
                .into(festival5Image);
        
        MainActivity activity = (MainActivity) getActivity();
        hotPlan = activity.hotPlan;
        hotSight = activity.hotSight;

        hotPlanName.setText(activity.hotPlan.getName());
        Picasso.with(getActivity())
                .load(ImageUtil.getTravelImageId())
                .resize(800, 600)
                .centerCrop()
                .into(hotPlanPicture);
        
        hotSightName.setText(activity.hotSight.getName());
        if (activity.hotSight.getPicture() != null && !activity.hotSight.getPicture().equals(""))
            Picasso.with(getActivity())
                    .load(activity.hotSight.getPicture())
                    .into(hotSightPicture);
    }
    
    @OnClick({R.id.festival_1, R.id.festival_2, R.id.festival_3, R.id.festival_4, R.id.festival_5})
    public void festivalClick (View view)  {
        Intent intent = FestivalActivity.getIntent(getActivity());
        switch (view.getId()) {
            case R.id.festival_1 :
                intent.putExtra("month", currentMonth);
                startActivity(intent);
                break;
            case R.id.festival_2 :
                intent.putExtra("month", currentMonth + 1);
                startActivity(intent);
                break;
            case R.id.festival_3 :
                intent.putExtra("month", currentMonth + 2);
                startActivity(intent);
                break;
            case R.id.festival_4 :
                intent.putExtra("month", currentMonth + 3);
                startActivity(intent);
                break;
            case R.id.festival_5 :
                intent.putExtra("month", currentMonth + 4);
                startActivity(intent);
                break;
            default:
                Toast.makeText(getContext(), "잘못된 접근입니다", Toast.LENGTH_SHORT).show();
        }
    }
    
    @OnClick(R.id.hot_plan_layout)
    public void hotPlanClick () {
        Intent intent = PlanActivity.getIntent(getActivity());
        intent.putExtra("planId", hotPlan.getId());
        intent.putExtra("other", 0);
        startActivity(intent);

    }
    
    @OnClick(R.id.hot_sight_layout)
    public void hotSightClick () {
        Intent intent = new Intent(getActivity(), SightDetailActivity.class);
        intent.putExtra("sightId", hotSight.getId());
        startActivity(intent);
    }
}
