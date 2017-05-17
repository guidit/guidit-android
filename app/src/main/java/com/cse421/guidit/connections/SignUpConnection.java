package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.SimpleConnectionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
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
    
    private SimpleConnectionEventListener listener;
    
    public void setListener(SimpleConnectionEventListener listener) {
        this.listener = listener;
    }
    
    // params : name, id, password
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();
        
        String result = "";
        
        String data =
                "name=" + params[0]
                + "&id=" + params[1]
                + "&password=" + params[2];
        Timber.d(data);
        
        String url = serverUrl + "/users/signup?";
        Timber.d("url : " + url);
    
        Request request = new Request.Builder()
                .url(url + data)
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
