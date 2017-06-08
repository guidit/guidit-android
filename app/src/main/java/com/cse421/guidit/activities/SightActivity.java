package com.cse421.guidit.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TabHost;
import com.cse421.guidit.R;
import com.cse421.guidit.vo.SightVo;
import java.util.ArrayList;
import butterknife.ButterKnife;

/**
 * Created by JEONGYI on 2017. 5. 10..
 */

public class SightActivity extends TabActivity {

    double X;
    double Y;

    public ArrayList<SightVo> sightList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);

        ButterKnife.bind(this);

        //basicX, basicY 데이터 받아오기
        Intent i = getIntent();
        String basicCity = i.getExtras().getString("basicCity");
        getXY(basicCity);

        //탭 만들기
        TabHost mTab = getTabHost();

        //XY 좌표 넘겨주기
        Intent mapIntent = new Intent(this,MapActivity.class);
        mapIntent.putExtra("basicX",X);
        mapIntent.putExtra("basicY",Y);
        mTab.addTab(mTab.newTabSpec("tab1").setIndicator("지도로 보기").setContent(mapIntent));
        mTab.addTab(mTab.newTabSpec("tab2").setIndicator("리스트로 보기").setContent(new Intent(this,SightListActivity.class)));

        sightList = new ArrayList<>();

    }

    private void getXY(String city) {
        switch(city){
            case "강남":
                X = 127.0475020;
                Y = 37.5173050;
                break;
            case "강북":
                X = 127.0254820;
                Y = 37.6397480;
                break;
            case "인천":
                X = 126.7051511;
                Y = 37.4560537;
                break;
            case "세종":
                X = 127.2890845;
                Y = 36.4800721;
                break;
            case "성남":
                X = 127.1263093;
                Y = 37.4201675;
                break;
            case "고양":
                X = 126.8319831;
                Y = 37.6583981;
                break;
            case "부천":
                X = 126.7660000;
                Y = 37.5035917;
                break;
            case "용인":
                X = 127.1774916;
                Y = 37.2412522;
                break;
            case "안산":
                X = 126.8308176;
                Y = 37.3219123;
                break;
            case "의정부":
                X = 127.0337530;
                Y = 37.7380830;
                break;
            case "남양주":
                X = 127.2164670;
                Y = 37.6359850;
                break;
            case "파주":
                X = 126.7798830;
                Y = 37.7601860;
                break;
            case "가평":
                X = 127.5095410;
                Y = 37.8315080;
                break;
            case "대전":
                X = 127.3846583;
                Y = 36.3504669;
                break;
            case "광주":
                X = 126.8513380;
                Y = 35.1600320;
                break;
            case "춘천":
                X = 127.7334390;
                Y = 37.8823420;
                break;
            case "원주":
                X = 127.9199210;
                Y = 37.3419480;
                break;
            case "강릉":
                X = 128.8758360;
                Y = 37.7521750;
                break;
            case "속초":
                X = 128.5918400;
                Y = 38.2071690;
                break;
            case "대구":
                X = 128.6017630;
                Y = 35.8713900;
                break;
            case "부샨":
                X = 129.0750223;
                Y = 35.1798160;
                break;
            case "울산":
                X = 129.3112381;
                Y = 35.5396493;
                break;
            case "충남":
                X = 126.6729080;
                Y = 36.6592490;
                break;
            case "충북":
                X = 127.4913605;
                Y = 36.6360995;
                break;
            case "경남":
                X = 128.6919403;
                Y = 35.2377974;
                break;
            case "경북":
                X = 128.5053474;
                Y = 36.5762027;
                break;
            case "전남":
                X = 126.4631714;
                Y = 34.8161102;
                break;
            case "전북":
                X = 127.1087840;
                Y = 35.8203294;
                break;
            case "제주":
                X = 126.5311380;
                Y = 33.4995680;
                break;
            case "서귀포":
                X = 126.5597875;
                Y = 33.2539250;
                break;
            default:
                break;
        }
    }
}
