package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.FjDetailBean;
import com.example.myapplication.Bean.Fobject;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends Activity {

    private int fjId;
    private Bitmap bitmap;
    private String fjName;
    private Fobject fobject;
    private View headView;
    private TextView companyText;
    private TextView regionText;
    private TextView actorText;
    private TextView focusText;
    private TextView fjAbstractText;
    private TextView typeText;
    private TextView timeText;
    private TextView updateTimeText;


    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //ButterKnife.bind(this);
        RecyclerView myRecycler = (RecyclerView) findViewById(R.id.myRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        myRecycler.setLayoutManager(manager);
        myRecycler.setHasFixedSize(true);

        Intent intent = getIntent();
        fjId = intent.getIntExtra("id", 0);
        byte[] bitMapByte = intent.getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(bitMapByte, 0, bitMapByte.length);
        fjName=intent.getStringExtra("fjname");
        Log.d("myapplog", "hahah:" + fjName);

        final List<String> content = new ArrayList<>();
        for (int i = 0; i < 30; i++)
            content.add(getListString(i));


        ParallaxRecyclerAdapter<String> stringAdapter = new ParallaxRecyclerAdapter<String>(content) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<String> adapter, int i) {
                // If you're using your custom handler (as you should of course)
                // you need to cast viewHolder to it.
                ((TextView) viewHolder.itemView).setText(content.get(i));
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<String> adapter, int i) {
                // Here is where you inflate your row and pass it to the constructor of your ViewHolder
                return new SimpleViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<String> adapter) {
                // return the content of your array
                return content.size();
            }
        };




        headView = getLayoutInflater().inflate(R.layout.details_header, myRecycler, false);
        ImageView headreImageView = headView.findViewById(R.id.imageView2);
        headreImageView.setImageBitmap(bitmap);
        TextView fjText = headView.findViewById(R.id.fjtitle);
        companyText=headView.findViewById(R.id.company);
        regionText=headView.findViewById(R.id.region);
        actorText=headView.findViewById(R.id.actor);
        focusText=headView.findViewById(R.id.focus);
        fjAbstractText=headView.findViewById(R.id.fjabstract);
        typeText=headView.findViewById(R.id.type);
        timeText=headView.findViewById(R.id.time);
        updateTimeText=headView.findViewById(R.id.updatetime);

        fjText.setText(fjName);


        stringAdapter.setParallaxHeader(headView, myRecycler);
        stringAdapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
                //TODO: implement toolbar alpha. See README for details
            }
        });
        stringAdapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View view, int i) {
                //TODO
                Toast.makeText(DetailsActivity.this, String.valueOf(i), Toast.LENGTH_LONG).show();
            }
        });
        myRecycler.setAdapter(stringAdapter);

        fobject = getData();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 200: {
                    companyText.setText("制作公司："+fobject.getCompany());
                    regionText.setText("地区："+fobject.getRegion());
                    actorText.setText("声优："+fobject.getActor());
                    typeText.setText("标签："+fobject.getType());
                    focusText.setText("看点："+fobject.getFocus());
                    fjAbstractText.setText("简介："+fobject.getObjectabstract());
                    if (fobject.getMonth()==201904)
                        timeText.setText("更新季度："+"2019年4月");
                    switch (fobject.getUpdatetime()) {
                        case 1:
                            updateTimeText.setText("更新时间：" + "每周日");
                            break;
                        case 2:
                            updateTimeText.setText("更新时间：" + "每周一");
                            break;
                        case 3:
                            updateTimeText.setText("更新时间："+"每周二");
                            break;
                        case 4:
                            updateTimeText.setText("更新时间："+"每周三");
                            break;
                        case 5:
                            updateTimeText.setText("更新时间："+"每周四");
                            break;
                        case 6:
                            updateTimeText.setText("更新时间："+"每周五");
                            break;
                        case 7:
                            updateTimeText.setText("更新时间："+"每周六");
                            break;
                        case 0:
                            updateTimeText.setText("更新时间："+"已完结");
                            default:
                                break;
                    }
                }
            }
        };
    };

    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public String getListString(int position) {
        return position + " - android";
    }



   private Fobject getData(){
    Request request = new Request.Builder()
            .url(MyApplication.getURL() + "fobject/byid?id=" + fjId)
            .build();

    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("myapplog", e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();

            FjDetailBean temp = GsonUtil.GsonToBean(body, FjDetailBean.class);
            Log.d("myapplog", "hahah1:" + temp.getData().toString());

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