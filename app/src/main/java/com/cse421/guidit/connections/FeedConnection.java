package com.cse421.guidit.connections;

import android.widget.Toast;

import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.vo.FeedVo;
import com.cse421.guidit.vo.UserVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by ho on 2017-06-02.
 */

public class FeedConnection extends BaseConnection {

    public static final int GET_LIST = 1111;
    public static final int CREATE = 2222;
    public static final int DELETE = 3333;

    private int mode;
    private ListConnectionListener<FeedVo> listConnectionListener;

    public FeedConnection(int mode) {
        this.mode = mode;
    }

    public void setListConnectionListener(ListConnectionListener listConnectionListener) {
        this.listConnectionListener = listConnectionListener;
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        String data, url, result;
        data = url = result = "";
        Request request;

        switch (mode) {
            case GET_LIST:
                // params = [city]
                data = "city=" + params[0];
                url = serverUrl + "/feed/list?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case CREATE:
                // params = [content, city]
                data = "id=" + UserVo.getInstance().getId()
                        + "&content=" + params[0]
                        + "&city=" + params[1];
                RequestBody body = RequestBody.create(HTML, data);
                url = serverUrl + "/feed/create";
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case DELETE:
                // params = [id]
                data = "id=" + params[0];
                url = serverUrl + "/feed/delete?";
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
        Timber.d("on post " + s);

        if (s.equals("")) {
            listener.connectionFailed();
        }

        try {
            switch (mode) {
                case GET_LIST:
                    //// TODO: 2017-06-02 리스트 받기
                    return;
                case CREATE:
                case DELETE:
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
        }
    }
}
