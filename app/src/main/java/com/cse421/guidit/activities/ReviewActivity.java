package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.DailyPlanRecyclerViewAdapter;
import com.cse421.guidit.callbacks.ImageUploadListener;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.ImgurConnection;
import com.cse421.guidit.connections.ReviewConnection;
import com.cse421.guidit.util.ImageUtil;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends AppCompatActivity {

    private final int REQ_PICK_CODE = 100;

    @BindView(R.id.date_count) TextView dateCount;
    @BindView(R.id.daily_plan_recycler) RecyclerView dailyPlanRecycler;
    @BindView(R.id.image_btn) TextView imageButton;
    @BindView(R.id.review_image) ImageView image;
    @BindView(R.id.review) EditText reviewInput;

    private int date, dailyPlanId;
    private String pictureUrl, review;
    private ArrayList<SightVo> sightList;
    private String imagePath, localImagePath;
    private DailyPlanRecyclerViewAdapter adapter;

    public static Intent getIntent (Context context) {
        return new Intent(context, ReviewActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ButterKnife.bind(this);

        imagePath = "";
        setViews();
    }

    private void setViews () {
        Intent intent = getIntent();
        date = intent.getIntExtra("date", 0);
        dailyPlanId = intent.getIntExtra("dailyPlanId", 0);
        sightList = intent.getParcelableArrayListExtra("sightList");
        pictureUrl = intent.getStringExtra("picture");
        review = intent.getStringExtra("review");

        dateCount.setText(date + "일차");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new DailyPlanRecyclerViewAdapter(this, DailyPlanRecyclerViewAdapter.from.ReviewActivity);
        adapter.setSightList(sightList);
        dailyPlanRecycler.setHasFixedSize(true);
        dailyPlanRecycler.setAdapter(adapter);
        dailyPlanRecycler.setLayoutManager(layoutManager);

        if (pictureUrl.equals("") || pictureUrl.equals("null")) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPicture();
                }
            });
        } else {
            imageButton.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(pictureUrl)
                    .into(image);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPicture();
            }
        });

        if (!review.equals("null"))
            reviewInput.setText(review);
    }

    private void getPicture () {
        Intent pickerIntent = new Intent(Intent.ACTION_PICK);
        pickerIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        pickerIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(pickerIntent, REQ_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PICK_CODE) {
            if (data == null) {
                Toast.makeText(this, "사진이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            imagePath = ImageUtil.getRealPathFromURI(this, data.getData());
            localImagePath = data.getData().toString();

            imageButton.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(localImagePath)
                    .into(image);
        }
    }

    @OnClick(R.id.review_confirm)
    public void confirm () {
        final String content = reviewInput.getText().toString();
        if (content.equals("")) {
            Toast.makeText(this, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();

        if (imagePath.equals("")) {
            ReviewConnection connection = new ReviewConnection();
            connection.setListener(new SimpleConnectionEventListener() {
                @Override
                public void connectionSuccess() {
                    progressBar.cancel();
                    Toast.makeText(ReviewActivity.this, "입력에 성공하였습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("image", localImagePath);
                    intent.putExtra("review", content);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void connectionFailed() {
                    progressBar.cancel();
                    Toast.makeText(ReviewActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            });
            connection.execute(dailyPlanId + "", content, "");
        } else {
            ImgurConnection connection = new ImgurConnection();
            connection.setListener(new ImageUploadListener() {
                @Override
                public void onSuccess(final String url) {
                    ReviewConnection connection = new ReviewConnection();
                    connection.setListener(new SimpleConnectionEventListener() {
                        @Override
                        public void connectionSuccess() {
                            progressBar.cancel();
                            Toast.makeText(ReviewActivity.this, "입력에 성공하였습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("image", url);
                            intent.putExtra("review", content);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void connectionFailed() {
                            progressBar.cancel();
                            Toast.makeText(ReviewActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    });
                    connection.execute(dailyPlanId + "", content, url);
                }

                @Override
                public void onFailed() {
                    Toast.makeText(ReviewActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                    progressBar.cancel();
                }
            });
            connection.execute(imagePath);
        }
    }
}
