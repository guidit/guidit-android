package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.vo.DailyPlanVo;
import com.cse421.guidit.vo.PlanVo;
import com.cse421.guidit.vo.SightVo;
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
 * Created by ho on 2017-06-04.
 */

public class PlanConnection extends BaseConnection {

    private modes mode;
    private ListConnectionListener<PlanVo> listConnectionListener;
    private ArrayList<DailyPlanVo> dailyPlanList;

    public PlanConnection(modes mode) {
        this.mode = mode;
    }

    public void setListConnectionListener(ListConnectionListener<PlanVo> listConnectionListener) {
        this.listConnectionListener = listConnectionListener;
    }

    public void setDailyPlanList(ArrayList<DailyPlanVo> dailyPlanList) {
        this.dailyPlanList = dailyPlanList;
    }

    public enum modes {
        MY_PLANS, ALL_PLANS, ADD_PLAN, DELETE_PLAN
    }

    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();

        String data, url, result;
        result = "";
        Request request = null;

        switch (mode) {
            case MY_PLANS:
                data = "id=" + UserVo.getInstance().getId();
                url = serverUrl + "/plan/list?";
                request = new Request.Builder()
                        .url(url)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case ALL_PLANS:
                data = "id=" + 0;   //// TODO: 2017-06-04 전체 리스트 불러오기 규약 정해야함
                url = serverUrl + "/plan/list?";
                request = new Request.Builder()
                        .url(url)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case ADD_PLAN:
                try {
                    JSONArray list = new JSONArray();
                    for (DailyPlanVo dailyPlanVo : dailyPlanList) {
                        JSONArray dailyArray = new JSONArray();
                        for (SightVo sightVo : dailyPlanVo.getSightList()) {
                            JSONObject dailyObject = new JSONObject();
                            dailyObject.put("id", sightVo.getId());
                            dailyArray.put(dailyObject);
                        }
                        list.put(dailyArray);
                    }

                    JSONObject object = new JSONObject();
                    object.put("name", strings[0]);
                    object.put("id", UserVo.getInstance().getId());
                    object.put("is_public", Boolean.valueOf(strings[1]));
                    object.put("daily_plan", list);

                    RequestBody body = RequestBody.create(JSON, object.toString());
                    url = serverUrl + "/plan/create";
                    request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case DELETE_PLAN:
                break;
            default:
                return "";
        }

        try {
            Response response = client.newCall(request).execute();
            result = response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s.equals("")) {
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
                case MY_PLANS:
                    ArrayList<PlanVo> planList = new ArrayList<>();
                    JSONArray list = new JSONArray(s);
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = list.getJSONObject(i);
                        PlanVo planVo = new PlanVo();

                        planVo.setId(object.getInt("id"));
                        planVo.setName(object.getString("name"));
                        planVo.setPublic(object.getBoolean("is_public"));
                        planVo.setViewCount(object.getInt("view_count"));
                        planList.add(planVo);
                    }
                    listConnectionListener.setList(planList);
                    break;
                case ALL_PLANS:
                    break;
                case ADD_PLAN:
                    JSONObject object = new JSONObject(s);
                    if (object.has("isSuccess")) {
                        if (object.getBoolean("isSuccess"))
                            listener.connectionSuccess();
                        else
                            listener.connectionFailed();
                    }
                    break;
                case DELETE_PLAN:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
