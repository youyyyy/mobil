package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.Delivery;
import com.example.myapplication.Bean.DeliveryBean;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private int fjId;
    private String url;

    private Delivery delivery;

    private ProgressDialog progressDialog;

    @BindView(R.id.webView)
    WebView delWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(WebActivity.this);
        Intent intent = getIntent();
        fjId=intent.getIntExtra("del_fjId",0);
        Log.d("myapplog", "fjid获取 "+fjId);

//        WebSettings settings = delWebView.getSettings();
////设置WebView支持JS
//        settings.setJavaScriptEnabled(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
////使用系统浏览器
//        delWebView.setWebViewClient(new WebViewClient());

        delWebView.setWebChromeClient(new WebChromeClient());
        delWebView.setWebViewClient(new WebViewClient());
        delWebView.getSettings().setJavaScriptEnabled(true);


        MyApplication.networkCheck();

        loading();

        //getData2();
    }

    private void loading(){
        progressDialog = new ProgressDialog(WebActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getText(R.string.please));
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getData2(progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 200: {
                    url=delivery.getUrl();
                    delWebView.loadUrl(url);
                    Toast.makeText(WebActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            });
        }
    };


    private Delivery getData2(ProgressDialog progressDialog){
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "delivery/byfobjectid?fobjectid=" + fjId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("myapplog", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();

                DeliveryBean temp = GsonUtil.GsonToBean(body, DeliveryBean.class);
                Log.d("myapplog", "Detail:" + temp.getData().toString());

                delivery = temp.getData();
                //fobject = GsonUtil.GsonToBean(temp.getData(), Fobject.class);
                Log.d("myapplog", "user:"+delivery.getUrl());
                Message msg = new Message();
                msg.what = 200;
                //msg.arg1 = 111;  可以设置arg1、arg2、obj等参数，传递这些数据
                //msg.arg2 = 222; msg.obj = obj;
                mHandler.sendMessage(msg);
            }
        });


        return delivery;

    }



}
