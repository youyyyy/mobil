package com.example.myapplication.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.Collect;
import com.example.myapplication.Bean.DiscussList;
import com.example.myapplication.Bean.DiscussReturn;
import com.example.myapplication.Bean.FjDetailBean;
import com.example.myapplication.Bean.Fobject;
import com.example.myapplication.Bean.User;
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
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.myapplication.Activity.RegistActivity.JSON;


/*
* 主要实现番剧具体信息展示以及评论展示
* */
public class DetailsActivity extends Activity {

    private int fjId;
    private Bitmap bitmap;
    private String fjName;
    private Fobject fobject;
    private User user;
    private View headView;
    private int uId;
    private int cookieid;
    private TextView companyText;
    private TextView regionText;
    private TextView actorText;
    private TextView focusText;
    private TextView fjAbstractText;
    private TextView typeText;
    private TextView timeText;
    private TextView updateTimeText;
    private ImageView disButton;
//    private Button deliveryButton;
    private ImageView parentImage;
    private CardView cardView2;
    private TextView fjText;
    private ImageView collectImage;

    private String userTrueName;
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
        fjId = intent.getIntExtra("fj_id", 0);
        byte[] bitMapByte = intent.getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(bitMapByte, 0, bitMapByte.length);
        //fjName = intent.getStringExtra("fj_name");

        cookieid=Integer.parseInt(getPreference(this,"id"));



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

        MyApplication.networkCheck();
       // initList();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        onCreate(null);
//    }

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
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH时");
                String tTime= format1.format(time);
                int pid=discuss.getUserid();
                if(cookieid==pid){
                    ((ViewHolder) viewHolder).userNameText.setTextColor(getColor(R.color.details1));
                    ((ViewHolder) viewHolder).userNameText.setText(discuss.getUsername()+":");
                }else {
                ((ViewHolder) viewHolder).userNameText.setText(discuss.getUsername()+":");
                }
                ((ViewHolder) viewHolder).bodyText.setText(discuss.getBody());
                ((ViewHolder)viewHolder).timeText.setText(tTime);
                ((ViewHolder)viewHolder).floorText.setText(floor+"楼");
                Log.d("myapplog", discuss.getUsername() + "id is:" + discuss.getUserid());
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<DiscussReturn> adapter, int i) {
                ViewHolder holder =  new ViewHolder(getLayoutInflater().inflate(R.layout.dicuss_item, viewGroup, false));
                holder.userNameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition() - 1;
                        DiscussReturn discuss = adapter.getData().get(position);
                        Log.d("myapplog", "you clicked :" + position);
                        Intent intent=new Intent(DetailsActivity.this, UserActivity.class);
                        intent.putExtra("user_userid",discuss.getUserid());
                        Log.d("myapplog", discuss.getUsername() + "id put in intent is:" + discuss.getUserid());
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
                Toast.makeText(DetailsActivity.this, "请点击名字哟~", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        headView = getLayoutInflater().inflate(R.layout.details_header, recyclerView, false);
        ImageView headreImageView = headView.findViewById(R.id.imageView2);
        headreImageView.setImageBitmap(bitmap);
        fjText = headView.findViewById(R.id.fjtitle);
        companyText=headView.findViewById(R.id.company);
        regionText=headView.findViewById(R.id.region);
        actorText=headView.findViewById(R.id.actor);
        focusText=headView.findViewById(R.id.focus);
        fjAbstractText=headView.findViewById(R.id.fjabstract);
        typeText=headView.findViewById(R.id.type);
        timeText=headView.findViewById(R.id.time);
        updateTimeText=headView.findViewById(R.id.updatetime);
        disButton=headView.findViewById(R.id.discuss);
//        deliveryButton=headView.findViewById(R.id.delivery);
        cardView2=headView.findViewById(R.id.cardview2);
        parentImage=headView.findViewById(R.id.imageView2);
        collectImage=headView.findViewById(R.id.collect);

        //fjText.setText(fjName);
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
                intent.putExtra("dis_fjId",fId);
                String userId=getPreference(v.getContext(), "id");
                intent.putExtra("dis_userid",userId);
                startActivity(intent);
                //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
            }
        });


//        deliveryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(DetailsActivity.this, WebActivity.class);
//                int fId=fjId;
//                intent.putExtra("del_fjId",fId);
//                startActivity(intent);
//                //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
//            }
//        });

        parentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailsActivity.this, WebActivity.class);
                int fId=fjId;
                intent.putExtra("del_fjId",fId);
                startActivity(intent);
                //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
            }
        });

        collectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collect();
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
                   fjText.setText(fobject.getTitle());
                    if(fobject.getCompany().equals("null"))
                        companyText.setText("制作公司："+"暂无数据~");
                    else
                        companyText.setText("制作公司："+fobject.getCompany());
                    regionText.setText("地区："+fobject.getRegion());
                    if(fobject.getActor().equals("null"))
                        actorText.setText("声优："+"暂无数据~");
                    else
                        actorText.setText("声优："+fobject.getActor());
                    typeText.setText("标签："+fobject.getType());
                    if(fobject.getFocus().equals("null"))
                        focusText.setText("看点："+"暂无数据~");
                    else
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
        }
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

    public void collect() {

        final ProgressDialog progressDialog = new ProgressDialog(DetailsActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.please));
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendCollect(progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendCollect(ProgressDialog progressDialog) throws Exception{
        client = new OkHttpClient.Builder()
                .build();


        String cuid=getPreference(this,"id");
        int cuId=Integer.valueOf(cuid);
        int cfId=fobject.getId();
        int cUpdatetime=fobject.getUpdatetime();
        String cFname=fobject.getTitle();

        Collect collect=new Collect();
        collect.setUserid(cuId);
        collect.setFjid(cfId);
        collect.setUpdatetime(cUpdatetime);
        collect.setFjname(cFname);




        String collectJson = GsonUtil.GsonString(collect);

        RequestBody requestBody = RequestBody.create(JSON, collectJson);

        Request request = new Request.Builder()
                .url(MyApplication.getURL() +"collect/add")
                .method("POST", requestBody)
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        Toast.makeText(DetailsActivity.this, getText(R.string.collectsuccess), Toast.LENGTH_LONG).show();
                    }
                });
//                startActivity(new Intent(RegistActivity.this, LoginActivity.class));
            }
        });

    }

    public static String getPreference(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        return preference.getString(key, "");
    }


}