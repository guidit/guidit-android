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
        String url, result = "";
        File file;
        MultipartBody.Builder builder;
        Request request;

        switch (mode) {
            case CREATE:
                //name, location, description, picture
                url = serverUrl + "/sight/foodtruckcreate";
                builder = new MultipartBody.Builder()
                        .addFormDataPart("id", UserVo.getInstance().getId() + "")
                        .addFormDataPart("name", strings[0])
                        .addFormDataPart("location", strings[1])
                        .addFormDataPart("description", strings[2]);
                if (!strings[3].equals(""))
                    builder.addFormDataPart("file", "foodtruck.png", RequestBody.create(MEDIA_TYPE_PNG, new File(strings[3])));
                request = new Request.Builder()
                        .url(url)
                        .post(builder.build())
                        .build();
                break;
            case UPDATE:
                //foodtruck_id, name, location, description,picture
                url = serverUrl + "/sight/foodtrucksetting";
                builder = new MultipartBody.Builder()
                        .addFormDataPart("id", strings[0])
                        .addFormDataPart("name", strings[1])
                        .addFormDataPart("location", strings[2])
                        .addFormDataPart("description", strings[3]);
                if (!strings[4].equals(""))
                    builder.addFormDataPart("file", "foodtruck.png", RequestBody.create(MEDIA_TYPE_PNG, new File(strings[4])));
                request = new Request.Builder()
                        .url(url)
                        .post(builder.build())
                        .build();
                break;
            case GET:
                String data = "id=" + UserVo.getInstance().getId();
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
                    sightVo.setName(object.getString("name"));
                    sightVo.setLocation(object.getString("location"));
                    sightVo.setInformation(object.getString("description"));
                    sightVo.setPicture(object.getString("picture"));
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
