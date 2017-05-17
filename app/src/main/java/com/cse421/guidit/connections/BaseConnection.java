package com.cse421.guidit.connections;

import android.os.AsyncTask;

/**
 * Created by hokyung on 2017. 5. 2..
 */

public abstract class BaseConnection extends AsyncTask<String, Void, String> {
    public final String serverUrl = "uni07.unist.ac.kr:9222";
}
