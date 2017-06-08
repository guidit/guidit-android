package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.SimpleConnectionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import timber.log.Timber;

/**
 * Created by hokyung on 2017. 5. 2..
 */

public class SignUpConnection extends BaseConnection {
    
    // params : name, id, password, image
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();
        
        String data, result = "";

        RequestBody body;
        data = "name=" + params[0]
                + "&id=" + params[1]
                + "&password=" + params[2];
        if (!params[3].equals("")) {
            data += "&file=" + params[3];
        }
        body = RequestBody.create(HTML, data);
        Timber.d("body " + body.toString());
        
        String url = serverUrl + "/users/signup";
        Timber.d("url : " + url);
    
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
        Timber.d("signup on post " + s);
    
        JSONObject isSuccess;
        
        try {
            isSuccess = new JSONObject(s);
            
            if (isSuccess.has("isSuccess")) {
                // 회원가입 성공
                listener.connectionSuccess();
            } else {
                // 회원가입 실패
                listener.connectionFailed();
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
