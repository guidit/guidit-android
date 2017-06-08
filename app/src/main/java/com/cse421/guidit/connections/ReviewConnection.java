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
        String data, result = "";

        RequestBody body;
        data = "id=" + strings[0]
                + "&review=" + strings[1];
        if (strings.length > 2) {
            data += "&file=" + strings[2];
        }
        body = RequestBody.create(HTML, data);
        Timber.d("body " + body.toString());

        String url = serverUrl + "/plan/review";

        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
