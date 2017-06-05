package com.cse421.guidit.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

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
}
