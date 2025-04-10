package com.example.icewarpassesttest;

import android.app.Application;
import android.content.Context;

public class MyIceWarpApplication extends Application {

    private static Context context;


    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }
    public static Context getContext() {
        return context.getApplicationContext();
    }
}
