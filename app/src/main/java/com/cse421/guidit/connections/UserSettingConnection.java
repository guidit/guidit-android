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
        
        String result = "";

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("id", UserVo.getInstance().getId() + "");
        builder.addFormDataPart("name", params[0]);
        if (!params[1].equals("")) {
            builder.addFormDataPart("password", params[2]);
        }
        if (!params[2].equals("")) {
            File file = new File(params[2]);
            builder.addFormDataPart("file", "profile.png", RequestBody.create(MEDIA_TYPE_PNG, file));
        }

        RequestBody body = builder.build();
        
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
