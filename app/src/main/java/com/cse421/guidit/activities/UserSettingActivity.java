package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.connections.SignUpConnection;
import com.cse421.guidit.connections.UserSettingConnection;
import com.cse421.guidit.util.CircleTransform;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.UserVo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserSettingActivity extends AppCompatActivity {
    
    // 갤러리에서 사진 가져오기 코드
    private final int REQ_PICK_CODE = 100;
    
    @BindView(R.id.profile_img) ImageView profileImage;
    @BindView(R.id.sign_up_name) EditText inputName;
    @BindView(R.id.sign_up_password) EditText inputPassword;
    @BindView(R.id.password_confirm) EditText inputPassword2;
    
    public static Intent getIntent (Context context) {
        return new Intent(context, UserSettingActivity.class);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
    
        ButterKnife.bind(this);
        
        setViews();
    }
    
    private void setViews () {
        UserVo userVo = UserVo.getInstance();
        
        if (userVo.getProfile().equals("testprofile")) {
            Picasso.with(this)
                    .load(userVo.getProfile())
                    .resize(800, 600)
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(profileImage);
        }
        inputName.setText(userVo.getName());
    }
    
    @OnClick(R.id.profile_img)
    public void getImage (View view) {
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
        
        Picasso.with(this)
                .load(data.getData().toString())
                .resize(800, 600)
                .centerCrop()
                .transform(new CircleTransform())
                .into(profileImage);
    }
    
    @OnClick(R.id.food_truck_btn)
    public void foodtruck (View view) {
        startActivity(FoodTruckActivity.getIntent(this));
    }
    
    @OnClick(R.id.sign_up_confirm)
    public void submit (View view) {
        final String name = inputName.getText().toString();
        String password = inputPassword.getText().toString();
        String password2 = inputPassword2.getText().toString();
        
        // 길이 확인
        if (name.length() == 0) {
            Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        
        // 비밀번호 확인
        if (!password.equals(password2)) {
            Toast.makeText(this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
            return;
        }
        
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();
        
        // 프로필 사진 파일 보내는 법 // TODO: 2017. 5. 8. 사진 전송
        
        // 다른 자료 보내는 법
        UserSettingConnection connection = new UserSettingConnection();
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                progressBar.cancel();
                Toast.makeText(UserSettingActivity.this, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show();
                UserVo userVo = UserVo.getInstance();
                userVo.setName(name);
                finish();
            }
            
            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(UserSettingActivity.this, "아이디가 중복됩니다", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(name, password);
    }
}
