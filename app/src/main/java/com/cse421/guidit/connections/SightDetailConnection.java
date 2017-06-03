package com.cse421.guidit.connections;

import com.cse421.guidit.activities.MapActivity;
import com.cse421.guidit.activities.SightDetailActivity;
import com.cse421.guidit.vo.SightVo;

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
 * Created by jeongyi on 2017. 5. 23..
 */

public class SightDetailConnection extends BaseConnection {

    private SightDetailActivity activity;

    public void setActivity(SightDetailActivity activity) {
        this.activity = activity;
    }

    // params : userId, sightId
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();
    
        String result = "";
    
        String data =
                "userId=" + params[0]
                + "&sightId=" + params[1];
        Timber.d(data);
        
        String url = serverUrl + "/sight/detail?";
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
        Timber.d("sightDetail on post " + s);
        // 실패시 id = -1
    
        JSONArray result;

        try {
            result = new JSONArray(s);
            JSONObject sightDetailJson = result.getJSONObject(0);

            SightVo sightVo = new SightVo();
            sightVo.setId(sightDetailJson.getInt("id"));
            sightVo.setName(sightDetailJson.getString("name"));
            sightVo.setPicture(sightDetailJson.getString("picture"));
            sightVo.setScore(sightDetailJson.getDouble("score"));
            sightVo.setInformation(sightDetailJson.getString("information"));
            sightVo.setFavorite(sightDetailJson.getBoolean("favorite"));

            activity.sightVo = sightVo;


        } catch (JSONException e) {
            e.printStackTrace();
            listener.connectionFailed();
        }

        listener.connectionSuccess();
    }
}
