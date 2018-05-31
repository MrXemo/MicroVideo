package com.micro.microvideo.app;

import android.app.Application;

/**
 * Created by hboxs006 on 2017/5/23.
 */

public class App extends Application {
    private static App instance;

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
