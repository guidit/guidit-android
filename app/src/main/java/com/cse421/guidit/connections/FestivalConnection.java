package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.vo.FestivalVo;

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
 * Created by ho on 2017-06-04.
 */

public class FestivalConnection extends BaseConnection {

    private ListConnectionListener listConnectionListener;

    public void setListConnectionListener(ListConnectionListener listConnectionListener) {
        this.listConnectionListener = listConnectionListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();

        String result = "";

        String data = "month=" + strings[0];
        Timber.d(data);

        String url = serverUrl + "/festival/list?";
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
        Timber.d("on post : " + s);

        if (s.equals("")) {
            listConnectionListener.connectionFailed();
            return;
        }

        try {
            JSONArray list = new JSONArray(s);
            ArrayList<FestivalVo> festivalList = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject object = list.getJSONObject(i);
                if (object.getInt("id") == -1) {
                    listConnectionListener.connectionFailed();
                    return;
                }

                FestivalVo festivalVo = new FestivalVo();
                festivalVo.setId(object.getInt("id"));
                festivalVo.setName(object.getString("name"));
                festivalVo.setDate(object.getString("date"));
                festivalVo.setPicture(object.getString("picture"));
                festivalVo.setScore(object.getDouble("score"));
                festivalList.add(festivalVo);
            }
            listConnectionListener.setList(festivalList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
