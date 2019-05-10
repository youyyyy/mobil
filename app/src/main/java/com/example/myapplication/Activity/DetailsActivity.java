package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.DiscussList;
import com.example.myapplication.Bean.DiscussReturn;
import com.example.myapplication.Bean.FjDetailBean;
import com.example.myapplication.Bean.Fobject;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/*
* 主要实现番剧具体信息展示以及评论展示
* */
public class DetailsActivity extends Activity {

    private int fjId;
    private Bitmap bitmap;
    private String fjName;
    private Fobject fobject;
    private View headView;
    private int uId;
    private TextView companyText;
    private TextView regionText;
    private TextView actorText;
    private TextView focusText;
    private TextView fjAbstractText;
    private TextView typeText;
    private TextView timeText;
    private TextView updateTimeText;
    private Button disButton;
    //private TextView userNameText;

    private List<DiscussReturn> content = new ArrayList<>();
    private RecyclerView myRecycler;

    private OkHttpClient client = new OkHttpClient();


/*
* 声明变量，初始化
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //ButterKnife.bind(this);
        myRecycler = (RecyclerView) findViewById(R.id.myRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //manager.setOrientation(RecyclerView.VERTICAL);
        //myRecycler.setLayoutManager(manager);
        //myRecycler.setHasFixedSize(true);


        Intent intent = getIntent();
        fjId = intent.getIntExtra("id", 0);
        byte[] bitMapByte = intent.getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(bitMapByte, 0, bitMapByte.length);
        fjName = intent.getStringExtra("fjname");


        new Thread(new Runnable() {
            @Override
            public void run() {
                content.clear();
                content.addAll(initList());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createAdapter(myRecycler, content);
                        getData();
                    }
                });
            }
        }).start();



       // initList();
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                content.clear();
//                content.addAll(initList());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        createAdapter(myRecycler, content);
//                        getData();
//                    }
//                });
//            }
//        }).start();
//
//    }
/*
* 评论信息显示
* */
    private void createAdapter(RecyclerView recyclerView, List<DiscussReturn> content) {
        final ParallaxRecyclerAdapter<DiscussReturn> adapter = new ParallaxRecyclerAdapter<DiscussReturn>(content) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<DiscussReturn> adapter, int i) {
               // ((ViewHolder) viewHolder).textView.setText(adapter.getData().get(i));
                DiscussReturn discuss = content.get(i);
                int floor=i+1;
                Date time=discuss.getTime();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH");
                String tTime= format1.format(time);
                uId=discuss.getUserid();
                Log.d("myapplog", "获得用户id "+uId);
                ((ViewHolder) viewHolder).userNameText.setText(discuss.getUsername()+":");
                ((ViewHolder) viewHolder).bodyText.setText(discuss.getBody());
                ((ViewHolder)viewHolder).timeText.setText(tTime);
                ((ViewHolder)viewHolder).floorText.setText(floor+"楼");
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<DiscussReturn> adapter, int i) {


                ViewHolder holder =  new ViewHolder(getLayoutInflater().inflate(R.layout.dicuss_item, viewGroup, false));
                holder.userNameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DetailsActivity.this, UserActivity.class);
                        intent.putExtra("userid",uId);
                        Log.d("myapplog", "onClick: "+uId);
                        startActivity(intent);
                        //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
                    }
                });
                return holder;
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<DiscussReturn> adapter) {
                return content.size();
            }
        };

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(DetailsActivity.this, "You clicked '" + position + "'", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headView = getLayoutInflater().inflate(R.layout.details_header, recyclerView, false);
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
        disButton=headView.findViewById(R.id.discuss);

        fjText.setText(fjName);
        Log.d("myapplog", "Title: " + fjName);
        adapter.setParallaxHeader(headView, recyclerView);
        adapter.setData(content);
        recyclerView.setAdapter(adapter);


        disButton=headView.findViewById(R.id.discuss);
        disButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailsActivity.this, DiscussActivity.class);
                int fId=fjId;
                intent.putExtra("fjId",fId);
                String userId=getPreference(v.getContext(), "id");
                intent.putExtra("userid",userId);
                startActivity(intent);
                //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
            }
        });



    }

    /*
    * 初始化textview
    * */
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameText;
        public TextView bodyText;
        public TextView timeText;
        public TextView floorText;

        public ViewHolder(View itemView) {
            super(itemView);
            userNameText = itemView.findViewById(R.id.username);
            bodyText = itemView.findViewById(R.id.dicuss_body);
            timeText=itemView.findViewById(R.id.dicuss_time);
            floorText=itemView.findViewById(R.id.floor);
        }

    }



    /*
    * 前端番剧信息赋值
    * */
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


    /*
    * 通过番剧id查找番剧具体信息
    * */
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



    /*
    * 获取本番剧的评论信息
    * */
    private List<DiscussReturn> initList () {
        List<DiscussReturn> discusses = new ArrayList<>();
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "discuss/fobject?fobjectid=" + fjId)
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

    public static String getPreference(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        return preference.getString(key, "");
    }


}