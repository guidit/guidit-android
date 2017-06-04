package com.cse421.guidit.fragments;

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
import com.cse421.guidit.activities.MainActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class BrowseFragment extends Fragment {
    
    @BindView(R.id.festival_present_image) ImageView festivalPresentImage;
    @BindView(R.id.festival_present_month) TextView festivalPresentMonth;
    @BindView(R.id.festival_future_image) ImageView festivalFutureImage;
    @BindView(R.id.festival_future_month) TextView festivalFutureMonth;
    @BindView(R.id.hot_plan_name) TextView hotPlanName;
    @BindView(R.id.hot_plan_picture) ImageView hotPlanPicture;
    @BindView(R.id.hot_sight_name) TextView hotSightName;
    @BindView(R.id.hot_sight_picture) ImageView hotSightPicture;
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
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        //todo 나머지 festival image 구하기
        festivalPresentMonth.setText(currentMonth + "월");
        Picasso.with(getActivity())
                .load(getFestivalImage(currentMonth))
                .into(festivalPresentImage);
        festivalFutureMonth.setText(currentMonth + 1 + "월");
        Picasso.with(getActivity())
                .load(getFestivalImage(currentMonth + 1))
                .into(festivalFutureImage);
        
        MainActivity activity = (MainActivity) getActivity();
    
        //// TODO: 2017. 5. 9. 여행계획의 대표 이미지도 달라고 해야겠다
        hotPlanName.setText(activity.hotPlan.getName());
        if (activity.hotPlan.getPicture() != null && !activity.hotPlan.getPicture().equals(""))
            Picasso.with(getActivity())
                    .load(activity.hotPlan.getPicture())
                    .into(hotPlanPicture);
        
        hotSightName.setText(activity.hotSight.getName());
        if (activity.hotSight.getPicture() != null && !activity.hotSight.getPicture().equals(""))
            Picasso.with(getActivity())
                    .load(activity.hotSight.getPicture())
                    .into(hotSightPicture);
    }
    
    private int getFestivalImage (int month) {
        switch (month) {
            case 5 :
                return R.drawable.may;
            case 6 :
                return R.drawable.june;
            case 7 :
                return R.drawable.july;
            default:
                return R.drawable.profile;
        }
    }
    
    @OnClick({R.id.festival_present, R.id.festival_future})
    public void festivalClick (View view)  {
        switch (view.getId()) {
            case R.id.festival_present :
                Toast.makeText(getContext(), "현재 축제", Toast.LENGTH_SHORT).show();
                break;
            case R.id.festival_future :
                Toast.makeText(getContext(), "미래 축제", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), "잘못된 접근", Toast.LENGTH_SHORT).show();
        }
    }
    
    @OnClick(R.id.hot_plan_layout)
    public void hotPlanClick () {
        Toast.makeText(getContext(), "hot plan으로 가자", Toast.LENGTH_SHORT).show();
    }
    
    @OnClick(R.id.hot_sight_layout)
    public void hotSightClick () {
        Toast.makeText(getContext(), "hot sight로 가자", Toast.LENGTH_SHORT).show();
    }
}
