package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.vo.UserVo;

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
    
    private SimpleConnectionEventListener listener;
    
    public void setListener(SimpleConnectionEventListener listener) {
        this.listener = listener;
    }
    
    // params : id, password
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();
    
        String result = "";
    
        String data =
                "id=" + params[0]
                + "&password=" + params[1];
    
        Timber.d(data);
    
        Request request = new Request.Builder()
                .url(serverUrl + "/users/user?" + data)
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
    
        JSONObject user;
        UserVo userVo = UserVo.getInstance();
    
        try {
            user = new JSONObject(s);
            
            userVo.setId(user.getInt("id"));
            userVo.setUser_id(user.getString("user_id"));
            userVo.setName(user.getString("name"));
            userVo.setProfile(user.getString("profile"));
            
        } catch (JSONException e) {
            e.printStackTrace();
            listener.connectionFailed();
        }
        
        listener.connectionSuccess();
    }
}
