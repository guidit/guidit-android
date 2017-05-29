package com.cse421.guidit.connections;

import com.cse421.guidit.activities.SightActivity;
import com.cse421.guidit.fragments.MapFragment;
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
 * Created by jeongyi on 2017. 5. 23..
 */

public class SightConnection extends BaseConnection {

    private MapFragment fragment;

    public void setFragment(MapFragment fragment) {
        this.fragment = fragment;
    }

    // params : X, Y
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();
    
        String result = "";
    
        String data =
                "X=" + params[0]
                + "&Y=" + params[1];
        Timber.d(data);
        
        String url = serverUrl + "/sight/sight?";
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
        Timber.d("sight on post " + s);
        // 실패시 id = -1
    
        JSONArray result;

        try {
            result = new JSONArray(s);
            JSONArray sightJson = result;

            ArrayList<SightVo> sightList = new ArrayList<>();
            for (int i = 0; i < sightJson.length(); i++) {
                JSONObject object = sightJson.getJSONObject(i);

                // TODO -- 해당범위 아무것도 없을때 (id=-1) 예외처리
                SightVo sightVo = new SightVo();
                sightVo.setId(object.getInt("id"));
                sightVo.setName(object.getString("name"));
                sightVo.setType(object.getString("type"));
                sightVo.setPicture(object.getString("picture"));
                sightVo.setScore(object.getDouble("score"));
                sightVo.setMapX(object.getDouble("locationX"));
                sightVo.setMapY(object.getDouble("locationY"));

                sightList.add(sightVo);
            }

            fragment.sightList = sightList;

        } catch (JSONException e) {
            e.printStackTrace();
            listener.connectionFailed();
        }

        listener.connectionSuccess();
    }
}
