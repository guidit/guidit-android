package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.PlanRecyclerAdapter;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.callbacks.SimpleListClickEventListener;
import com.cse421.guidit.connections.PlanConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.DailyPlanVo;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlanActivity extends AppCompatActivity {

    public final int REQUEST_SELECTED_SIGHT = 8859;

    @BindView(R.id.plan_name) EditText nameInput;
    @BindView(R.id.plan_recycler) RecyclerView planRecyclerView;
    @BindView(R.id.login_btn) ImageView lockButton;

    private ArrayList<DailyPlanVo> dailyPlanList;
    private PlanRecyclerAdapter adapter;
    private int selectedDate;
    private boolean isPublic;

    public static Intent getIntent (Context context) {
        return new Intent(context, PlanActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ButterKnife.bind(this);

        setViews();
    }

    private void setViews () {
        //// TODO: 2017-06-03 여행계획 수정으로 들어왔을 때의 동작 추가
        dailyPlanList = new ArrayList<>();
        dailyPlanList.add(new DailyPlanVo());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new PlanRecyclerAdapter(
                this,
                new SimpleListClickEventListener() {
                    @Override
                    public void itemClicked(int position) {
                        selectedDate = position;
                        startActivityForResult(SelectSightActivity.getIntent(PlanActivity.this), REQUEST_SELECTED_SIGHT);
                    }
                }
        );
        adapter.setList(dailyPlanList);
        planRecyclerView.setHasFixedSize(true);
        planRecyclerView.setAdapter(adapter);
        planRecyclerView.setLayoutManager(layoutManager);

        isPublic = true;
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPublic = !isPublic;
                if (isPublic) {
                    Picasso.with(PlanActivity.this)
                            .load(R.drawable.ic_lock_open)
                            .into(lockButton);
                } else {
                    Picasso.with(PlanActivity.this)
                            .load(R.drawable.ic_lock)
                            .into(lockButton);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECTED_SIGHT) {
            if (resultCode == RESULT_OK) {
                ArrayList<SightVo> selectedList = data.getParcelableArrayListExtra("list");
                dailyPlanList.get(selectedDate).setSightList(selectedList);
                adapter.notifyInnerAdapter(selectedDate);
            }
        }
    }

    @OnClick(R.id.add_date_btn)
    public void addDate () {
        dailyPlanList.add(new DailyPlanVo());
        adapter.setList(dailyPlanList);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.plan_confirm)
    public void confirm () {
        if (nameInput.getText().toString().equals("")) {
            Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dailyPlanList.size() == 1 && dailyPlanList.get(0).getSightList().size() == 0) {
            Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();
        PlanConnection connection = new PlanConnection(PlanConnection.modes.ADD_PLAN);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                progressBar.cancel();
                Toast.makeText(PlanActivity.this, "일정이 저장되었습니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(PlanActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.setDailyPlanList(dailyPlanList);
        connection.execute(nameInput.getText().toString(), isPublic + "");
    }
}
