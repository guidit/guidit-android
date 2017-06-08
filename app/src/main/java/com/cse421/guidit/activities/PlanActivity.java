package com.cse421.guidit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse421.guidit.R;
import com.cse421.guidit.adapters.PlanRecyclerAdapter;
import com.cse421.guidit.callbacks.PlanClickEventListener;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.callbacks.SingleObjectConnectionListener;
import com.cse421.guidit.connections.PlanConnection;
import com.cse421.guidit.util.ProgressBarDialogUtil;
import com.cse421.guidit.vo.DailyPlanVo;
import com.cse421.guidit.vo.PlanVo;
import com.cse421.guidit.vo.SightVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlanActivity extends AppCompatActivity {

    public final int REQUEST_SELECTED_SIGHT = 8859;
    public final int REQUEST_WRITE_REVIEW = 9957;
    public final int NEW = 0;
    public final int EXIST = 1;
    public final int OTHER = 2;

    @BindView(R.id.plan_name) EditText nameInput;
    @BindView(R.id.plan_recycler) RecyclerView planRecyclerView;
    @BindView(R.id.public_btn) ImageView lockButton;
    @BindView(R.id.add_date_btn) TextView addDateButton;
    @BindView(R.id.plan_confirm) TextView confirmButton;

    private ArrayList<DailyPlanVo> dailyPlanList;
    private PlanRecyclerAdapter adapter;
    private PlanVo planVo;
    private int selectedDate, mode;
    private boolean isPublic;

    public static Intent getIntent (Context context) {
        return new Intent(context, PlanActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ButterKnife.bind(this);

        getExistData();
    }

    private void getExistData () {
        Intent intent = getIntent();
        if (intent.hasExtra("planId")) {
            if (intent.hasExtra("other"))
                mode = OTHER;
            else
                mode = EXIST;
            planVo = new PlanVo();
            planVo.setId(intent.getIntExtra("planId", 0));
            final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
            progressBar.show();
            PlanConnection connection = new PlanConnection(PlanConnection.modes.GET_PLAN);
            connection.setSingleObjectConnectionListener(new SingleObjectConnectionListener() {
                @Override
                public void connectionSuccess(Object object) {
                    progressBar.cancel();
                    planVo = (PlanVo) object;
                    dailyPlanList = planVo.getDailyPlanList();
                    isPublic = planVo.isPublic();
                    setViews();
                }

                @Override
                public void connectionFailed() {
                    progressBar.cancel();
                    Toast.makeText(PlanActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void notExist() {
                    progressBar.cancel();
                }
            });
            connection.execute(intent.getIntExtra("planId", 0) + "");

            if (mode == OTHER) {
                addCount();
            }
        } else {
            mode = NEW;
            dailyPlanList = new ArrayList<>();
            dailyPlanList.add(new DailyPlanVo());
            isPublic = true;
            setViews();
        }

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/BMJUA_ttf.ttf");
        confirmButton.setTypeface(type);
        addDateButton.setTypeface(type);
    }

    private void addCount () {
        final ProgressBarDialogUtil progressBar = new ProgressBarDialogUtil(this);
        progressBar.show();

        PlanConnection connection = new PlanConnection(PlanConnection.modes.ADD_COUNT);
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                progressBar.cancel();
                // do nothing
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(PlanActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.execute(planVo.getId() + "");
    }

    private void setViews () {
        if (planVo != null) {
            nameInput.setText(planVo.getName().toString());
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new PlanRecyclerAdapter(
                this,
                new PlanClickEventListener() {
                    @Override
                    public void changeDailyPlan(int position) {
                        selectedDate = position;
                        startActivityForResult(SelectSightActivity.getIntent(PlanActivity.this), REQUEST_SELECTED_SIGHT);
                    }

                    @Override
                    public void writeReview(int position) {
                        selectedDate = position;
                        Intent intent = ReviewActivity.getIntent(PlanActivity.this);
                        intent.putExtra("date", selectedDate);
                        intent.putExtra("dailyPlanId", dailyPlanList.get(position).getId());
                        intent.putExtra("sightList", dailyPlanList.get(position).getSightList());
                        intent.putExtra("picture", dailyPlanList.get(position).getPicture());
                        intent.putExtra("review", dailyPlanList.get(position).getReview());
                        startActivityForResult(intent, REQUEST_WRITE_REVIEW);
                    }
                }
        );
        adapter.setList(dailyPlanList);

        if (mode == EXIST)
            adapter.setMode(PlanRecyclerAdapter.Mode.EXIST);
        else if (mode == NEW)
            adapter.setMode(PlanRecyclerAdapter.Mode.NEW);
        else
            adapter.setMode(PlanRecyclerAdapter.Mode.OTHER);

        planRecyclerView.setHasFixedSize(true);
        planRecyclerView.setAdapter(adapter);
        planRecyclerView.setLayoutManager(layoutManager);

        if (isPublic) {
            lockButton.setImageResource(R.drawable.ic_lock_open);
        } else {
            lockButton.setImageResource(R.drawable.ic_lock);
        }
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPublic = !isPublic;
                if (isPublic) {
                    lockButton.setImageResource(R.drawable.ic_lock_open);
                } else {
                    lockButton.setImageResource(R.drawable.ic_lock);
                }
            }
        });

        // 이 여행계획이 내꺼가 아닐 경우
        if (mode == OTHER) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            nameInput.setEnabled(false);
            lockButton.setEnabled(false);
            addDateButton.setVisibility(View.GONE);
            confirmButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECTED_SIGHT) {
            if (resultCode == RESULT_OK) {
                ArrayList<SightVo> selectedList = data.getParcelableArrayListExtra("list");
                dailyPlanList.get(selectedDate).setSightList(selectedList);
                adapter.changeInnerRecycler(selectedDate, selectedList);
            }
        }
        if (requestCode == REQUEST_WRITE_REVIEW) {
            if (resultCode == RESULT_OK) {
                dailyPlanList.get(selectedDate).setPicture(data.getStringExtra("image"));
                dailyPlanList.get(selectedDate).setReview(data.getStringExtra("review"));
                adapter.notifyDataSetChanged();
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
        PlanConnection connection = null;
        if (mode == EXIST) {
            connection = new PlanConnection(PlanConnection.modes.MODIFY_PLAN);
        } else if (mode == NEW){
            connection = new PlanConnection(PlanConnection.modes.ADD_PLAN);
        } else {
            return;
        }
        connection.setListener(new SimpleConnectionEventListener() {
            @Override
            public void connectionSuccess() {
                progressBar.cancel();
                Toast.makeText(PlanActivity.this, "일정이 저장되었습니다", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void connectionFailed() {
                progressBar.cancel();
                Toast.makeText(PlanActivity.this, "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });
        connection.setDailyPlanList(dailyPlanList);
        if (mode == EXIST) {
            connection.execute(planVo.getId() + "", nameInput.getText().toString(), isPublic + "");
        } else if (mode == NEW){
            connection.execute(nameInput.getText().toString(), isPublic + "");
        }
    }
}
