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
    
    private SimpleConnectionEventListener listener;
    
    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    
    public void setListener(SimpleConnectionEventListener listener) {
        this.listener = listener;
    }
    
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();
    
        String result = "";
    
        String data = "id=" + UserVo.getInstance().getId();
        Timber.d(data);
        
        String url = serverUrl + "/main?";
    
        Request request = new Request.Builder()
                .url(serverUrl + "/main?" + data)
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
            JSONArray feedsJson = main.getJSONArray(2);
            JSONArray myPlansJson = main.getJSONArray(3);
            
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
    
            ArrayList<FeedVo> feedList = new ArrayList<>();
            for (int i = 0; i < feedsJson.length(); i++) {
                JSONObject object = feedsJson.getJSONObject(i);
                
                FeedVo feedVo = new FeedVo();
                feedVo.setId(object.getInt("id"));
                feedVo.setContent(object.getString("content"));
                feedVo.setCity(object.getString("city"));
                feedVo.setDate(object.getString("date"));
                feedVo.setUserName(object.getString("user_name"));
                feedVo.setProfile(object.getString("profile"));
                
                feedList.add(feedVo);
            }
            activity.feedList = feedList;
            
            ArrayList<PlanVo> myPlanList = new ArrayList<>();
            for (int i = 0; i < myPlansJson.length(); i++) {
                JSONObject object = myPlansJson.getJSONObject(i);
                
                PlanVo planVo = new PlanVo();
                planVo.setId(object.getInt("id"));
                planVo.setName(object.getString("name"));
                planVo.setPublic(object.getBoolean("is_public"));
                planVo.setViewCount(object.getInt("view_count"));
                
                myPlanList.add(planVo);
            }
            activity.myPlanList = myPlanList;
            
        } catch (JSONException e) {
            e.printStackTrace();
            listener.connectionFailed();
        }
    
        listener.connectionSuccess();
    }
}
