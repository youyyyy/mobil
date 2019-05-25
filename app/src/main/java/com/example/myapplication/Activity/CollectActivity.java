package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.SelfCollectAdapter;
import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.Collect;
import com.example.myapplication.Bean.CollectList;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CollectActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private String selfId;

    private List<Collect> selfCollect=new ArrayList<>();
    private SelfCollectAdapter selfCollectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        ButterKnife.bind(CollectActivity.this);
        Intent intent = getIntent();
        selfId=intent.getStringExtra("self_uid");
        Log.d("myapplog", "getid:"+selfId);

        RecyclerView recyclerView = findViewById(R.id.self_collect_recyvleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        selfCollectAdapter = new SelfCollectAdapter(selfCollect);
        recyclerView.setAdapter(selfCollectAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                selfCollect.clear();
                selfCollect.addAll(initList());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selfCollectAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private List<Collect> initList () {
        List<Collect> collect = new ArrayList<>();
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "collect/self?userid=" + selfId)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String body = response.body().string();
            Log.d("myapplog", "return "+body);
            CollectList collectList = GsonUtil.GsonToBean(body, CollectList.class);
            collect = collectList.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return collect;
    }
}
