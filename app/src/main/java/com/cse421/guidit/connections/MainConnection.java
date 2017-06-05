package com.cse421.guidit.connections;

import com.cse421.guidit.activities.MainActivity;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
import com.cse421.guidit.vo.FeedVo;
import com.cse421.guidit.vo.PlanVo;
import com.cse421.guidit.vo.SightVo;
import com.cse421.guidit.vo.UserVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by hokyung on 2017. 5. 8..
 */

public class MainConnection extends BaseConnection {
    
    // 전송 받은 데이터를 액티비티의 변수에 저장하기위해
    private MainActivity activity;
    
    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();
    
        String result = "";
        
        String url = serverUrl + "/main";
    
        Request request = new Request.Builder()
                .url(url)
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
    
        try {
            JSONArray main = new JSONArray(s);
            JSONObject hotPlanJson = main.getJSONObject(0);
            JSONObject hotSightJson = main.getJSONObject(1);
            
            PlanVo hotPlanVo = new PlanVo();
            hotPlanVo.setId(hotPlanJson.getInt("id"));
            hotPlanVo.setName(hotPlanJson.getString("name"));
            hotPlanVo.setPublic(true);
            hotPlanVo.setViewCount(hotPlanJson.getInt("view_count"));
            activity.hotPlan = hotPlanVo;
    
            SightVo sightVo = new SightVo();
            sightVo.setId(hotSightJson.getInt("id"));
            sightVo.setName(hotSightJson.getString("name"));
            sightVo.setLocation(hotSightJson.getString("location"));
            sightVo.setType(hotSightJson.getString("type"));
            sightVo.setInformation(hotSightJson.getString("information"));
            sightVo.setPicture(hotSightJson.getString("picture"));
            sightVo.setScore(hotSightJson.getDouble("score"));
            activity.hotSight = sightVo;
            
        } catch (JSONException e) {
            e.printStackTrace();
            listener.connectionFailed();
        }
    
        listener.connectionSuccess();
    }
}
