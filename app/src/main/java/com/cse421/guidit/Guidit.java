package com.cse421.guidit;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import timber.log.Timber;

/**
 * Created by hokyung on 2017. 4. 30..
 */

public class Guidit extends Application {
    private static Guidit instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        Timber.plant(new Timber.DebugTree());
    }

    public static Context getContext() {
        return instance;
    }
}
