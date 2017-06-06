package com.cse421.guidit.connections;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by ho on 2017-06-06.
 */

public class ReviewConnection extends BaseConnection {
    //strings: dailyPlanId, review, file
    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        String result = "";

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", strings[0])
                .addFormDataPart("review", strings[1]);
        if (!strings[2].equals("")) {
            File file = new File(strings[2]);
            builder.addFormDataPart("file", "picture.png", RequestBody.create(MEDIA_TYPE_PNG, file));
        }

        String url = serverUrl + "/plan/review";

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        Timber.d(s);

        JSONObject isSuccess;

        try {
            isSuccess = new JSONObject(s);
            if (isSuccess.has("isSuccess")) {
                if (isSuccess.getBoolean("isSuccess"))
                    listener.connectionSuccess();
                else
                    listener.connectionFailed();
            } else {
                listener.connectionFailed();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
