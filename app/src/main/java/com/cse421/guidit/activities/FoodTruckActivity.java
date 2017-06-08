package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodTruckActivity extends AppCompatActivity {

    //// TODO: 2017-05-21 일단 이미지는 하나만
    @BindView(R.id.food_truck_img) ImageView imageInput;
    @BindView(R.id.food_truck_image_text) TextView imageText;
    @BindView(R.id.food_truck_name) EditText nameInput;
    @BindView(R.id.food_truck_location) EditText locationInput;
    @BindView(R.id.food_truck_description) EditText descriptionInput;
    @BindView(R.id.food_truck_auth_btn) TextView authBtn;

    private int imageCount;

    public static Intent getIntent (Context context) {
        return new Intent(context, FoodTruckActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck);

        ButterKnife.bind(this);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");
        imageText.setTypeface(type);
        authBtn.setTypeface(type);

        imageCount = 0;
        //// TODO: 2017-05-21 이미 푸드트럭 등록한 경우 그 정보 입력하기
    }

    @OnClick(R.id.food_truck_image_add)
    public void addImage () {
        //// TODO: 2017-05-21 푸드트럭 이미지 서버로 전송
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //// TODO: 2017-05-21 푸드트럭 이미지 받기
    }

    @OnClick(R.id.food_truck_auth_btn)
    public void auth () {
        if (imageCount == 0) {
            Toast.makeText(this, "이미지를 넣어주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nameInput.getText().toString().equals("")) {
            Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (locationInput.getText().toString().equals("")) {
            Toast.makeText(this, "지역을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descriptionInput.getText().toString().equals("")) {
            Toast.makeText(this, "설명을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        //// TODO: 2017-05-21 푸드트럭 정보 서버로 전송
    }
}
