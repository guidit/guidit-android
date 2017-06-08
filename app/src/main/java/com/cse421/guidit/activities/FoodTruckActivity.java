package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.ImageUploadListener;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.callbacks.SingleObjectConnectionListener;
import com.cse421.guidit.connections.FoodTruckConnection;
import com.cse421.guidit.connections.ImgurConnection;
import com.cse421.guidit.util.ImageUtil;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class FoodTruckActivity extends AppCompatActivity {

    private final int REQ_PICK_CODE = 8834;

    @BindView(R.id.food_truck_img) ImageView imageInput;
    @BindView(R.id.food_truck_image_add) LinearLayout imageText;
    @BindView(R.id.food_truck_name) EditText nameInput;
    @BindView(R.id.food_truck_location) EditText locationInput;
    @BindView(R.id.food_truck_description) EditText descriptionInput;
    @BindView(R.id.food_truck_auth_btn) TextView authBtn;

    private String originImage, newImage;
    private SightVo foodTruck;
    private boolean isUpdate;

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

        originImage = newImage = "";
        getExistData();
    }

    private void getExistData () {
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();
        FoodTruckConnection connection = new FoodTruckConnection(FoodTruckConnection.Modes.GET);
        connection.setSingleObjectConnectionListener(new SingleObjectConnectionListener() {
            @Override
            public void connectionSuccess(Object object) {
                progressBar.cancel();
                foodTruck = (SightVo) object;
                originImage = foodTruck.getPicture();
                setViews();
                isUpdate = true;
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                isUpdate = false;
            }

            @Override
            public void notExist() {
                progressBar.cancel();
            }
        });
        connection.execute();
    }

    private void setViews () {
        Picasso.with(this)
                .load(originImage)
                .resize(800, 600)
                .centerCrop()
                .into(imageInput);
        imageText.setVisibility(GONE);
        nameInput.setText(foodTruck.getName());
        locationInput.setText(foodTruck.getLocation());
        descriptionInput.setText(foodTruck.getInformation());
    }

    @OnClick(R.id.food_truck_img)
    public void imageClicked () {
        addImage();
    }

    @OnClick(R.id.food_truck_image_add)
    public void textClicked () {
        addImage();
    }

    private void addImage () {
        Intent pickerIntent = new Intent(Intent.ACTION_PICK);
        pickerIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        pickerIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(pickerIntent, REQ_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Toast.makeText(this, "사진이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        newImage = ImageUtil.getRealPathFromURI(this, data.getData());

        Picasso.with(this)
                .load(data.getData().toString())
                .resize(800, 600)
                .centerCrop()
                .into(imageInput);
        imageText.setVisibility(GONE);
    }

    @OnClick(R.id.food_truck_auth_btn)
    public void auth () {
        if (originImage.equals("") && newImage.equals("")) {
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

        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();

        // 기존 이미지 사용
        if (newImage.equals("")) {
            FoodTruckConnection connection;
            connection = new FoodTruckConnection(FoodTruckConnection.Modes.UPDATE);
            connection.setListener(new SimpleConnectionEventListener() {
                @Override
                public void connectionSuccess() {
                    progressBar.cancel();
                    Toast.makeText(FoodTruckActivity.this, "등록되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void connectionFailed() {
                    progressBar.cancel();
                    Toast.makeText(FoodTruckActivity.this, "인터넷 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            });

            connection.execute(
                    foodTruck.getId() + "",
                    nameInput.getText().toString(),
                    locationInput.getText().toString(),
                    descriptionInput.getText().toString(),
                    ""
            );
        } else {
            // 새 이미지 사용
            ImgurConnection connection = new ImgurConnection();
            connection.setListener(new ImageUploadListener() {
                @Override
                public void onSuccess(String url) {
                    progressBar.cancel();

                    FoodTruckConnection connection;
                    if (isUpdate) {
                        connection = new FoodTruckConnection(FoodTruckConnection.Modes.UPDATE);
                    } else {
                        connection = new FoodTruckConnection(FoodTruckConnection.Modes.CREATE);
                    }
                    connection.setListener(new SimpleConnectionEventListener() {
                        @Override
                        public void connectionSuccess() {
                            progressBar.cancel();
                            Toast.makeText(FoodTruckActivity.this, "등록되었습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void connectionFailed() {
                            progressBar.cancel();
                            Toast.makeText(FoodTruckActivity.this, "인터넷 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (isUpdate) {
                        connection.execute(
                                foodTruck.getId() + "",
                                nameInput.getText().toString(),
                                locationInput.getText().toString(),
                                descriptionInput.getText().toString(),
                                url
                        );
                    } else {
                        connection.execute(
                                nameInput.getText().toString(),
                                locationInput.getText().toString(),
                                descriptionInput.getText().toString(),
                                url
                        );
                    }
                }

                @Override
                public void onFailed() {
                    Toast.makeText(FoodTruckActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                    progressBar.cancel();
                }
            });
            connection.execute(newImage);
        }
    }
}
