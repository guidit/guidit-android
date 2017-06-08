package com.cse421.guidit.connections;

import com.cse421.guidit.vo.UserVo;

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
 * Created by hokyung on 2017. 5. 18..
 */

public class UserSettingConnection extends BaseConnection {
    
    // params : name, password, image
    @Override
    protected String doInBackground(String... params) {
        
        OkHttpClient client = new OkHttpClient();
        
        String data, result = "";

        RequestBody body;
        data = "id=" + UserVo.getInstance().getId()
                + "&name=" + params[0];
        if (!params[1].equals("")) {
            data +="&password=" + params[1];
        }
        if (!params[2].equals("")) {
            data += "&file=" + params[2];
        }
        body = RequestBody.create(HTML, data);
        Timber.d("body " + body.toString());
        
        String url = serverUrl + "/users/setting";
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
        Timber.d("usersetting on post " + s);
        
        JSONObject isSuccess;
        
        try {
            isSuccess = new JSONObject(s);
            
            if (isSuccess.has("isSuccess"))
                if (isSuccess.getBoolean("isSuccess"))
                    listener.connectionSuccess();
                else
                    listener.connectionFailed();
            else
                listener.connectionFailed();
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
