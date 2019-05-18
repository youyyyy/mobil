package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.FjDetailBean;
import com.example.myapplication.Bean.Fobject;
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

public class DiscussDetailsActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private int fJId;
    private Fobject fobject;

    @BindView(R.id.discuss_fjtitle)
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_details);
        ButterKnife.bind(DiscussDetailsActivity.this);
        Intent intent = getIntent();
        fJId=intent.getIntExtra("fj_Id", 0);
        Log.d("myapplog", "getid:"+fJId);

        getData();

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 200: {
                    titleText.setText(fobject.getTitle());
                    }
                }
            }
    };

    private Fobject getData(){
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "fobject/byid?id=" + fJId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("myapplog", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("myapplog", "body: " + body);

                FjDetailBean temp = GsonUtil.GsonToBean(body, FjDetailBean.class);
                Log.d("myapplog", "Detail:" + temp.getData().toString());

                fobject = temp.getData();
                //fobject = GsonUtil.GsonToBean(temp.getData(), Fobject.class);
                Log.d("myapplog", fobject.getCompany());
                Message msg = new Message();
                msg.what = 200;
                //msg.arg1 = 111;  可以设置arg1、arg2、obj等参数，传递这些数据
                //msg.arg2 = 222; msg.obj = obj;
                mHandler.sendMessage(msg);
            }
        });
        return fobject;
    }
}
