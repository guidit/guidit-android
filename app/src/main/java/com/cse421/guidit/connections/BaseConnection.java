package com.cse421.guidit.connections;

import android.os.AsyncTask;

import okhttp3.MediaType;

/**
 * Created by hokyung on 2017. 5. 2..
 */

public abstract class BaseConnection extends AsyncTask<String, Void, String> {
    public final String serverUrl = "http://uni07.unist.ac.kr:9222";
    public final MediaType HTML = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
}
