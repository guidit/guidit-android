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

    public static final int GET_DETAIL = 1111;
    public static final int FAVORITE = 2222;

    private SightDetailActivity activity;
    private int mode;

    public SightDetailConnection(int mode) {
        this.mode = mode;
    }
    public void setActivity(SightDetailActivity activity) {
        this.activity = activity;
    }

    // params : userId, sightId
    @Override
    protected String doInBackground(String... params) {
    
        OkHttpClient client = new OkHttpClient();

        String data, url, result;
        data = url = result = "";
        Request request;

        switch(mode){
            case GET_DETAIL:
                data = "userId=" + params[0]
                       + "&sightId=" + params[1];
                url = serverUrl + "/sight/detail?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case FAVORITE:
                data = "userId=" + params[0]
                        + "&sightId=" + params[1]
                        + "&favorite=" + params[2];
                url = serverUrl + "/sight/favorite?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            default:
                return "";

        }
    
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
            switch (mode) {
                case GET_DETAIL:
                    result = new JSONArray(s);
                    JSONObject sightDetailJson = result.getJSONObject(0);

                    if(sightDetailJson.getInt("id") == -1){
                        listener.connectionFailed();
                    }else {
                        SightVo sightVo = new SightVo();
                        sightVo.setId(sightDetailJson.getInt("id"));
                        sightVo.setName(sightDetailJson.getString("name"));
                        sightVo.setPicture(sightDetailJson.getString("picture"));
                        sightVo.setScore(sightDetailJson.getDouble("score"));
                        sightVo.setInformation(sightDetailJson.getString("information"));
                        sightVo.setFavorite(sightDetailJson.getBoolean("favorite"));

                        activity.sightVo = sightVo;
                        listener.connectionSuccess();
                    }
                    return;
                case FAVORITE:
                    JSONObject isSuccess = new JSONObject(s);
                    if (isSuccess.has("isSuccess"))
                        if (isSuccess.getBoolean("isSuccess"))
                            listener.connectionSuccess();
                        else
                            listener.connectionFailed();
                    else
                        listener.connectionFailed();
                    return;
                default:
                    return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            listener.connectionFailed();
        }

        listener.connectionSuccess();
    }
}
