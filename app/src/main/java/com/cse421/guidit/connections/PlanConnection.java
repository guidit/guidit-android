package com.cse421.guidit.connections;

import com.cse421.guidit.callbacks.ListConnectionListener;
import com.cse421.guidit.callbacks.SingleObjectConnectionListener;
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
    private ArrayList<DailyPlanVo> dailyPlanList;
    private ListConnectionListener<PlanVo> listConnectionListener;
    private SingleObjectConnectionListener singleObjectConnectionListener;

    public PlanConnection(modes mode) {
        this.mode = mode;
    }

    public void setDailyPlanList(ArrayList<DailyPlanVo> dailyPlanList) {
        this.dailyPlanList = dailyPlanList;
    }

    public void setListConnectionListener(ListConnectionListener<PlanVo> listConnectionListener) {
        this.listConnectionListener = listConnectionListener;
    }

    public void setSingleObjectConnectionListener(SingleObjectConnectionListener singleObjectConnectionListener) {
        this.singleObjectConnectionListener = singleObjectConnectionListener;
    }

    public enum modes {
        MY_PLANS, ALL_PLANS, ADD_PLAN, GET_PLAN, MODIFY_PLAN
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
                        .url(url + data)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case ALL_PLANS:
                data = "id=" + 0;
                url = serverUrl + "/plan/list?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case ADD_PLAN:
                try {
                    JSONArray list = new JSONArray();
                    for (int i = 0; i < dailyPlanList.size(); i++) {
                        DailyPlanVo dailyPlanVo = dailyPlanList.get(i);
                        JSONArray sightArray = new JSONArray();
                        for (SightVo sightVo : dailyPlanVo.getSightList()) {
                            JSONObject dailyObject = new JSONObject();
                            dailyObject.put("id", sightVo.getId());
                            sightArray.put(dailyObject);
                        }
                        JSONObject dailyObject = new JSONObject();
                        dailyObject.put("day_num", i);
                        dailyObject.put("sight_list", sightArray);
                        list.put(dailyObject);
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
            case GET_PLAN:
                data = "id=" + strings[0];
                url = serverUrl + "/plan/detail?";
                request = new Request.Builder()
                        .url(url + data)
                        .build();
                Timber.d("url:" + url + " / data:" + data);
                break;
            case MODIFY_PLAN:
                try {
                    JSONArray list = new JSONArray();
                    for (int i = 0; i < dailyPlanList.size(); i++) {
                        DailyPlanVo dailyPlanVo = dailyPlanList.get(i);
                        JSONArray sightArray = new JSONArray();
                        for (SightVo sightVo : dailyPlanVo.getSightList()) {
                            JSONObject dailyObject = new JSONObject();
                            dailyObject.put("id", sightVo.getId());
                            sightArray.put(dailyObject);
                        }
                        JSONObject dailyObject = new JSONObject();
                        dailyObject.put("day_num", i);
                        dailyObject.put("sight_list", sightArray);
                        list.put(dailyObject);
                    }

                    JSONObject object = new JSONObject();
                    object.put("id", strings[0]);
                    object.put("name", strings[1]);
                    object.put("is_public", Boolean.valueOf(strings[2]));
                    object.put("daily_plan", list);

                    RequestBody body = RequestBody.create(JSON, object.toString());
                    url = serverUrl + "/plan/modify";
                    request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            } else if (listConnectionListener != null) {
                listConnectionListener.connectionFailed();
                return;
            } else {
                singleObjectConnectionListener.connectionFailed();
                return;
            }
        }

        try {
            switch (mode) {
                case MY_PLANS:
                case ALL_PLANS:
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
                case ADD_PLAN:
                case MODIFY_PLAN:
                    JSONObject object = new JSONObject(s);
                    if (object.has("isSuccess")) {
                        if (object.getBoolean("isSuccess"))
                            listener.connectionSuccess();
                        else
                            listener.connectionFailed();
                    } else {
                        listener.connectionFailed();
                    }
                    break;
                case GET_PLAN:
                    JSONObject plan = new JSONObject(s);
                    PlanVo planVo = new PlanVo();
                    planVo.setId(plan.getInt("id"));
                    planVo.setName(plan.getString("name"));
                    planVo.setViewCount(plan.getInt("view_count"));

                    ArrayList<DailyPlanVo> dailyPlanList = new ArrayList<>();
                    JSONArray dailyPlanArray = plan.getJSONArray("daily_plan");
                    for (int i = 0; i < dailyPlanArray.length(); i++) {
                        DailyPlanVo dailyPlanVo = new DailyPlanVo();
                        JSONObject dailyObject = dailyPlanArray.getJSONObject(i);
                        dailyPlanVo.setId(dailyObject.getInt("id"));
                        dailyPlanVo.setDayNum(dailyObject.getInt("day_num"));
                        dailyPlanVo.setReview(dailyObject.getString("review"));
                        dailyPlanVo.setPicture(dailyObject.getString("picture"));

                        ArrayList<SightVo> sightList = new ArrayList<>();
                        JSONArray sightArray = dailyObject.getJSONArray("sight_list");
                        for (int j = 0; j < sightArray.length(); j++) {
                            SightVo sightVo = new SightVo();
                            JSONObject sightObject = sightArray.getJSONObject(j);
                            sightVo.setId(sightObject.getInt("id"));
                            sightVo.setName(sightObject.getString("name"));

                            sightList.add(sightVo);
                        }
                        dailyPlanVo.setSightList(sightList);

                        dailyPlanList.add(dailyPlanVo);
                    }
                    planVo.setDailyPlanList(dailyPlanList);

                    singleObjectConnectionListener.connectionSuccess(planVo);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.connectionFailed();
                return;
            } else if (listConnectionListener != null) {
                listConnectionListener.connectionFailed();
                return;
            } else {
                singleObjectConnectionListener.connectionFailed();
                return;
            }
        }
    }
}
