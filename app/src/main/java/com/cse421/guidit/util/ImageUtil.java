package com.cse421.guidit.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import com.cse421.guidit.R;

import java.util.Random;

/**
 * Created by ho on 2017-06-05.
 */

public class ImageUtil {
    public static String getRealPathFromURI(Context context, Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String result = cursor.getString(colum_index);

        // 커서 반환
        cursor.close();
        cursorLoader.cancelLoadInBackground();

        return result;
    }

    public static int getFestivalImage (int month) {
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

    public static int getTravelImageId () {
        int pictures[] = {
                R.drawable.travel1,
                R.drawable.travel2,
                R.drawable.travel3,
                R.drawable.travel4,
                R.drawable.travel5,
                R.drawable.travel6
        };
        Random random = new Random();
        int target = random.nextInt(pictures.length);
        return pictures[target];
    }
}
