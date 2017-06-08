package com.cse421.guidit.connections;

import com.cse421.guidit.activities.MapActivity;
import com.cse421.guidit.activities.SightDetailActivity;
import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.vo.CommentVo;
import com.cse421.guidit.vo.FeedVo;
import com.cse421.guidit.vo.UserVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by jeongyi on 2017-06-07.
 */

public class CommentConnection extends BaseConnection {

    public static final int GET_LIST = 1111;
    public static final int CREATE = 2222;

    private int mode;
    private ListConnectionListener<CommentVo> listConnectionListener;

    public CommentConnection(int mode) {
        this.mode = mode;
    }
    private SightDetailActivity activity;

    public void setActivity(SightDetailActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        String data, url, result;
        data = url = result = "";
        Request request;

        switch (mode) {
            case GET_LIST:
                data = "sightId=" + params[0];
                url = serverUrl + "/sight/commentlist?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case CREATE:
                // params = [content, city]
                data = "userId=" + UserVo.getInstance().getId()
                        + "&sightId=" + params[1]
                        + "&comment=" + params[2]
                        + "&date=" + params[3];
                RequestBody body = RequestBody.create(HTML, data);
                url = serverUrl + "/sight/commentcreate";
                request = new Request.Builder()
                        .url(url)
                        .post(body)
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

        try {
            switch (mode) {
                case GET_LIST:
                    ArrayList<CommentVo> commentList = new ArrayList<>();
                    JSONArray list = new JSONArray(s);
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = list.getJSONObject(i);
                        CommentVo commentVo = new CommentVo();

                        commentVo.setId(object.getInt("id"));
                        commentVo.setUserProfile(object.getString("profile"));
                        commentVo.setUserId(object.getString("user_id"));
                        commentVo.setComment(object.getString("comment"));
                        commentVo.setDate(object.getString("date"));
                        commentList.add(commentVo);
                    }
                    activity.commentList = commentList;
                    listener.connectionSuccess();

                    return;
                case CREATE:
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
