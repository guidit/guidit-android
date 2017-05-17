package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.vo.UserVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by hokyung on 2017. 5. 2..
 */

public class LoginConnection extends BaseConnection {
    
    // params : id, password
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();
    
        String result = "";
    
        String data =
                "id=" + params[0]
                + "&password=" + params[1];
        Timber.d(data);
        
        String url = serverUrl + "/users/login?";
        Timber.d(url);
    
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
        Timber.d("login on post " + s);
        // 실패시 id = -1
    
        JSONArray result;
        JSONObject user;
        UserVo userVo = UserVo.getInstance();
    
        try {
            result = new JSONArray(s);
            user = result.getJSONObject(0);
            
            userVo.setId(user.getInt("id"));
            if (userVo.getId() == -1) {
                listener.connectionFailed();
            } else {
                userVo.setUser_id(user.getString("user_id"));
                userVo.setName(user.getString("name"));
                userVo.setProfile(user.getString("profile"));
                
                listener.connectionSuccess();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
