package com.cse421.guidit.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;

import com.cse421.guidit.R;

/**
 * Created by hokyung on 2016. 11. 14..
 * 다이얼로그를 편하게 쓰기 위한 유틸 클래스
 */

public class ProgressBarDialogUtil {

    private Context mContext;
    private AppCompatDialog mDialog;

    public ProgressBarDialogUtil(Context context) {
        mContext = context;
    }

    // 다이얼로그를 보여준다
    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 프로그래스바 다이얼로그는 취소키로 없어지면 안되니까
        builder.setCancelable(false)
                .setView(R.layout.dialog_progressbar);
        mDialog = builder.create();
        mDialog.show();
        // 배경 투명하게 처리
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    // 다이얼로그를 없앤다
    public void cancel() {
        mDialog.cancel();
    }
}
