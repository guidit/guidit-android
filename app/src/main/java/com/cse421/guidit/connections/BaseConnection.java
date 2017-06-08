package com.cse421.guidit.connections;

import android.os.AsyncTask;

import com.cse421.guidit.callbacks.SimpleConnectionEventListener;

import okhttp3.MediaType;

/**
 * Created by hokyung on 2017. 5. 2..
 */

public abstract class BaseConnection extends AsyncTask<String, Void, String> {
    protected final String CLIENT_ID = "5f8f25e914dcbf5";
    protected final String IMGUR_SERVER = "https://api.imgur.com/3/image";
    protected final String serverUrl = "http://uni07.unist.ac.kr:9222";
    protected final MediaType HTML = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    protected final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    protected SimpleConnectionEventListener listener;
    public void setListener(SimpleConnectionEventListener listener) {
        this.listener = listener;
    }
}
