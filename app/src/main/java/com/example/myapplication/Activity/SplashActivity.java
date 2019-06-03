package com.example.myapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private String body;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                getTest();
            }
        }).start();
*/
        MyApplication.networkCheck();
        SystemClock.sleep(1500);
        loginCheck();

    }

    private void loginCheck() {
        SharedPreferences sharedPreferences = getSharedPreferences("cookies", MODE_PRIVATE);
        Long date = sharedPreferences.getLong("expires", 1);
        if (date - System.currentTimeMillis() < 0 ) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } else  {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

        }
    }

//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch(msg.what){
//                case 200: {
//                        Toast.makeText(SplashActivity.this, getText(R.string.client), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }
//    };

        private void getTest(){

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SplashActivity.this, getText(R.string.client), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SplashActivity.this, getText(R.string.client), Toast.LENGTH_LONG).show();
                    }
                });
            }




            }
//        });

    }




