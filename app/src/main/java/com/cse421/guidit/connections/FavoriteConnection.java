package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.ListConnectionListener;
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

    private Modes mode;
    private ListConnectionListener listConnectionListener;

    public enum Modes {
        GET_LIST, DELETE
    }

    public FavoriteConnection(Modes mode) {
        this.mode = mode;
    }

    public void setListConnectionListener(ListConnectionListener listConnectionListener) {
        this.listConnectionListener = listConnectionListener;
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        String data, url, result = "";
        Request request;

        switch (mode) {
            case GET_LIST:
                data = "id=" + UserVo.getInstance().getId();
                url = serverUrl + "/favorite/list?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
                break;
            case DELETE:
                data = "userId=" + UserVo.getInstance().getId()
                        + "&sightId=" + params[0]
                        + "&favorite=" + false;
                url = serverUrl + "/sight/favorite?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
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
        Timber.d("favorite on post " + s);

        if (s.equals("") || s.startsWith("<!")) {
            if (listener != null) {
                listener.connectionFailed();
                return;
            } else {
                listConnectionListener.connectionFailed();
                return;
            }
        }

        try {
            switch (mode) {
                case GET_LIST:
                    JSONArray favoriteList = new JSONArray(s);
                    ArrayList<SightVo> favorites = new ArrayList<>();

                    for (int i = 0; i < favoriteList.length(); i++) {
                        JSONObject data = favoriteList.getJSONObject(i);

                        SightVo sightVo = new SightVo();
                        sightVo.setId(data.getInt("id"));
                        sightVo.setName(data.getString("name"));
                        sightVo.setInformation(data.getString("information"));
                        sightVo.setPicture(data.getString("picture"));
                        sightVo.setMapX(data.getDouble("x"));
                        sightVo.setMapY(data.getDouble("y"));
                        favorites.add(sightVo);
                    }
                    listConnectionListener.setList(favorites);
                    break;
                case DELETE:
                    JSONObject object = new JSONObject(s);
                    if (object.has("isSuccess")) {
                        if (object.getBoolean("isSuccess")) {
                            listener.connectionSuccess();
                        } else {
                            listener.connectionFailed();
                        }
                    } else {
                        listener.connectionFailed();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
