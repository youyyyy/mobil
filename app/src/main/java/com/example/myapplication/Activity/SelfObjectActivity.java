package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.baoyz.widget.PullRefreshLayout;
import com.example.myapplication.Adapter.SelfObjectAdapter;
import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.FJList;
import com.example.myapplication.Bean.Fobject;
import com.example.myapplication.Bean.FobjectList;
import com.example.myapplication.Bean.User;
import com.example.myapplication.Bean.UserDiscussBean;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelfObjectActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private String selfId;
    private User user;
    private String type;
    private String[] types;
    private String typeUrl = "";

    private List<FJList> tempfjLists = new ArrayList<>();
    private List<FJList> fjLists = new ArrayList<>();
    private SelfObjectAdapter selfObjectAdapter;

    @BindView(R.id.self_suggest_fj)
    androidx.constraintlayout.widget.ConstraintLayout selfSuggest;
    @BindView(R.id.self_object_refreash)
    com.baoyz.widget.PullRefreshLayout self_object_refreash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_object);
        ButterKnife.bind(SelfObjectActivity.this);
        selfSuggest.setBackground(getDrawable(R.drawable.self_suggest));
        Intent intent = getIntent();
        selfId=intent.getStringExtra("self_uid");
        Log.d("myapplog", "getid:"+selfId);

        RecyclerView recyclerView = findViewById(R.id.self_object_recyvleView );
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        selfObjectAdapter = new SelfObjectAdapter(fjLists);
        recyclerView.setAdapter(selfObjectAdapter);

        self_object_refreash.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyApplication.networkCheck();
                        getData();
                        self_object_refreash.setRefreshing(false);
                    }
                }).start();
            }
        });


        getData();

        MyApplication.networkCheck();
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 200: {
                    type=user.getType();
                    Log.d("myapplog", "gettype:"+type);
                    types=type.split("、|,");
                    int i = 0;
                    for(String x: types) {
                        switch (i) {
                            case 0:
                                typeUrl +=  "type1=" + x;
                                break;
                            case 1:
                                typeUrl += "&type2=" + x;
                                break;
                            case 2:
                                typeUrl += "&type3=" + x;
                                break;
                        }
                        ++i;
                    }
                    initList();
                }
            }
        };
    };



    private User getData(){
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "user/showuser?id=" + selfId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("myapplog", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();

                UserDiscussBean temp = GsonUtil.GsonToBean(body, UserDiscussBean.class);
                Log.d("myapplog", "Detail:" + temp.getData().toString());

                user = temp.getData();
                //fobject = GsonUtil.GsonToBean(temp.getData(), Fobject.class);
                Log.d("myapplog", user.getUsername());
                Message msg = new Message();
                msg.what = 200;
                //msg.arg1 = 111;  可以设置arg1、arg2、obj等参数，传递这些数据
                //msg.arg2 = 222; msg.obj = obj;
                mHandler.sendMessage(msg);
            }
        });
        return user;
    }

    private List<FJList> getNewList() {
        return tempfjLists;
    }

    private void initList () {
        String URL;
        if (type.equals("null"))
            URL=MyApplication.getURL() + "fobject/bytype?type1=" + "搞笑";
        else
            URL=MyApplication.getURL() + "fobject/bytype?" + typeUrl;
            Log.d("myapplog", "geturl:"+URL);

        //TODO
        //开始动画
        Request request = new Request.Builder()
                .url(URL)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("myapplog", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("myapplog", "hahah:" + body);

                FobjectList temp= GsonUtil.GsonToBean(body, FobjectList.class);
                List<Fobject> fobjectList = temp.getData();
                tempfjLists.clear();
                for (Fobject f: fobjectList) {
                    tempfjLists.add(getImage(f));
                }
                fjLists.clear();
                fjLists.addAll(getNewList());


                //TODO
                //保存这个list到本地
                // SharedPreferences sp = mBaseActivity.getSharedPreferences("getNewList", MODE_PRIVATE);//创建sp对象
//                Gson gson = new Gson();
//                String jsonStr=gson.toJson(fjLists); //将List转换成Json
//                SharedPreferences.Editor editor =  getSharedPreferences("spNewList",MODE_PRIVATE).edit();
//                editor.putString("KEY_getNewList", jsonStr) ; //存入json串
//                editor.commit() ;  //提交

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO
                        //停止动画
                        selfObjectAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private FJList getImage(Fobject fobject){
        FJList temp = new FJList();
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "fobject/get?id=" + fobject.getId())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            InputStream inputStream = response.body().byteStream();
            Bitmap tempPic = BitmapFactory.decodeStream(inputStream);
            temp.setImage(tempPic);
            temp.setName(fobject.getTitle());
            temp.setId(fobject.getId());
            //tempfjLists.add(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }


}
