package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.ImageUploadListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by ho on 2017-06-08.
 */

public class ImgurConnection extends BaseConnection {

    private ImageUploadListener listener;

    public void setListener(ImageUploadListener listener) {
        this.listener = listener;
    }

    // strings: imagePath, title, description
    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        String result = "";

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File(strings[0])))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + CLIENT_ID)
                .url(IMGUR_SERVER)
                .post(requestBody)
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
        super.onPostExecute(s);
        Timber.d("onpost " + s);

        if (s == null || s.equals("")) {
            listener.onFailed();
        }

        try {
            JSONObject object = new JSONObject(s);
            boolean isSuccess = object.getBoolean("success");
            if (isSuccess) {
                JSONObject data = object.getJSONObject("data");
                String link = data.getString("link");
                listener.onSuccess(link);
            } else {
                listener.onFailed();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailed();
        }
    }
}
