package com.example.myapplication.Application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0: {
                    Toast.makeText(mContext, mContext.getText(R.string.client), Toast.LENGTH_LONG).show();
                }
            }
        };
    };

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

    public static void networkCheck() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getTest();
            }
        }).start();

    }
    private static void getTest(){
        int flag = 1;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "client/test")
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String body = response.body().string();
            Log.d("myapplog", "return "+body);
            if (!body.equals("1")) {
                flag = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            flag = 0;
            Message msg = new Message();
            msg.what = flag;
            mHandler.sendMessage(msg);
        }
        Message msg = new Message();
        msg.what = flag;
        mHandler.sendMessage(msg);
    }


}
