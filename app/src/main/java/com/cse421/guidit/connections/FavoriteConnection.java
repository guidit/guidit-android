package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.FavoriteConnectionEventListener;
import com.cse421.guidit.callbacks.SimpleConnectionEventListener;
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
 * Created by ho on 2017-05-21.
 */

public class FavoriteConnection extends BaseConnection {

    private FavoriteConnectionEventListener favoriteListener;

    public void setListener (FavoriteConnectionEventListener listener) {
        favoriteListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        OkHttpClient client = new OkHttpClient();

        String result = "";

        String data =
                "id=" + UserVo.getInstance().getId();
        Timber.d(data);

        String url = serverUrl + "/users/favorite?";
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
        Timber.d("favorite on post " + s);

        JSONArray favoriteList;
        ArrayList<SightVo> favorites = new ArrayList<>();

        try {
            favoriteList = new JSONArray(s);

            for (int i = 0; i < favoriteList.length(); i++) {
                JSONObject data = favoriteList.getJSONObject(i);

                SightVo sightVo = new SightVo();
                sightVo.setId(data.getInt("id"));
                sightVo.setName(data.getString("name"));
                sightVo.setType(data.getString("type"));
                sightVo.setInformation(data.getString("information"));
                sightVo.setLocation(data.getString("location"));
                sightVo.setPicture(data.getString("picture"));
                sightVo.setScore(data.getDouble("score"));

                favorites.add(sightVo);
            }

            favoriteListener.onSuccess(favorites);

        } catch (JSONException e) {
            e.printStackTrace();
            favoriteListener.onFailed();
        }
    }
}
