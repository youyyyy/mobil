package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.SelfDiscussAdapter;
import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.DiscussList;
import com.example.myapplication.Bean.DiscussReturn;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelfDiscussActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private String selfId;

    private List<DiscussReturn> selfDis=new ArrayList<>();
    private SelfDiscussAdapter selfDiscussAdapter;

    @BindView(R.id.my_dis)
    androidx.constraintlayout.widget.ConstraintLayout myDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_discuss);

        ButterKnife.bind(SelfDiscussActivity.this);
        myDis.setBackground(getDrawable(R.drawable.my_dis));
        Intent intent = getIntent();
        selfId=intent.getStringExtra("self_uid");
        Log.d("myapplog", "getid:"+selfId);

        RecyclerView recyclerView = findViewById(R.id.self_discuss_recyvleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        selfDiscussAdapter = new SelfDiscussAdapter(selfDis);
        recyclerView.setAdapter(selfDiscussAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                selfDis.clear();
                selfDis.addAll(initList());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selfDiscussAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    private List<DiscussReturn> initList () {
        List<DiscussReturn> discusses = new ArrayList<>();
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "discuss/self?userid=" + selfId)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String body = response.body().string();
            Log.d("myapplog", "return "+body);
            DiscussList discussList = GsonUtil.GsonToBean(body, DiscussList.class);
            discusses = discussList.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return discusses;
    }
}
