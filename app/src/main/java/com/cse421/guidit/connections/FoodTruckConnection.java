package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.SingleObjectConnectionListener;
import com.cse421.guidit.vo.SightVo;
import com.cse421.guidit.vo.UserVo;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by ho on 2017-06-05.
 */

public class FoodTruckConnection extends BaseConnection {

    private Modes mode;
    private SingleObjectConnectionListener singleObjectConnectionListener;

    public enum Modes {
        CREATE, UPDATE, GET
    }

    public FoodTruckConnection (Modes modes) {
        this.mode = modes;
    }

    public void setSingleObjectConnectionListener(SingleObjectConnectionListener singleObjectConnectionListener) {
        this.singleObjectConnectionListener = singleObjectConnectionListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        String url, data, result = "";
        MultipartBody.Builder builder;
        RequestBody body;
        Request request;

        switch (mode) {
            case CREATE:
                //name, location, description, picture
                data = "user_id=" + UserVo.getInstance().getId()
                        + "&name=" + strings[0]
                        + "&location=" + strings[1]
                        + "&description=" + strings[2]
                        + "&file=" + strings[3];
                body = RequestBody.create(HTML, data);
                url = serverUrl + "/sight/foodtruckcreate";
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                break;
            case UPDATE:
                //foodtruck_id, name, location, description,picture
                data = "id=" + strings[0]
                        + "&name=" + strings[1]
                        + "&location=" + strings[2]
                        + "&description=" + strings[3];
                if (!strings[4].equals("")) {
                    data += "&file=" + strings[4];
                }
                body = RequestBody.create(HTML, data);
                url = serverUrl + "/sight/foodtrucksetting";
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                break;
            case GET:
                data = "id=" + UserVo.getInstance().getId();
                url = serverUrl + "/sight/foodtruck?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
                break;
            default:
                return result;
        };

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
        Timber.d("on post " + s);
        if (s.equals("")) {
            if (listener != null)
                listener.connectionFailed();
            else
                singleObjectConnectionListener.connectionFailed();
            return;
        }

        JSONObject object;
        try {
            switch (mode) {
                case CREATE:
                case UPDATE:
                    object = new JSONObject(s);
                    if (object.getBoolean("isSuccess"))
                        listener.connectionSuccess();
                    else
                        listener.connectionFailed();
                    break;
                case GET:
                    object = new JSONObject(s);
                    SightVo sightVo = new SightVo();
                    sightVo.setId(object.getInt("id"));
                    if (sightVo.getId() == -1) {
                        singleObjectConnectionListener.notExist();
                        return;
                    }
                    sightVo.setName(object.getString("name"));
                    sightVo.setLocation(object.getString("location"));
                    sightVo.setInformation(object.getString("information"));
                    sightVo.setPicture(object.getString("picture"));
//                    sightVo.setUserId(object.getString("user_id"));
                    singleObjectConnectionListener.connectionSuccess(sightVo);
                    break;
                default:
                    if (listener != null)
                        listener.connectionFailed();
                    else
                        singleObjectConnectionListener.connectionFailed();
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
