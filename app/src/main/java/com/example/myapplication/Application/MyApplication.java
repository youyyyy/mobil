package com.example.myapplication.Application;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context mContext;
    private static  String URL;
    private static Boolean haveRun = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        URL = "http://192.168.137.1:8070/";
    }

    public static Context getInstance() {
        return mContext;
    }

    public static String getURL() {
        return URL;
    }

    public static Boolean getHaveRun() {
        return haveRun;
    }

    public static void setHaveRun(Boolean haveRun) {
        MyApplication.haveRun = haveRun;
    }
}
