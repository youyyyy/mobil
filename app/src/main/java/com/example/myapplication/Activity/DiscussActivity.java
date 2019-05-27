package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.Discuss;
import com.example.myapplication.Bean.Music;
import com.example.myapplication.Bean.MusicAPI;
import com.example.myapplication.Bean.MusicAPIBean;
import com.example.myapplication.Bean.MusicBean;
import com.example.myapplication.Bean.User;
import com.example.myapplication.Bean.UserDiscussBean;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.myapplication.Activity.RegistActivity.JSON;


/*
* 主要实现评论的书写与发送
* */
public class DiscussActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private User user;
    private Music music;
    private MusicAPI musicAPI;

    private int fId;
    private String userId;
    private String userName;
    private String fjName;
    private String musicURL = "";

    @BindView(R.id.edit_discuss)
    EditText eDiscussText;
    @BindView(R.id.send_discuss)
    Button sDiscussButton;
    @BindView(R.id.music_name)
    TextView musicNameText;
    @BindView(R.id.music_singer)
    TextView musicSingerText;
    @BindView(R.id.music_card)
    CardView musicCard;
    @BindView(R.id.music_web)
    WebView musicWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        ButterKnife.bind(DiscussActivity.this);
        Intent intent = getIntent();
        fId=intent.getIntExtra("dis_fjId",0);
        userId=intent.getStringExtra("dis_userid");

        musicWeb.setWebChromeClient(new WebChromeClient());
        musicWeb.setWebViewClient(new WebViewClient());
        musicWeb.getSettings().setJavaScriptEnabled(true);

        sDiscussButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        musicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!musicURL.isEmpty()) {
                    music();
                }
            }
        });
        getMusicData3();
        getUserData2();
    }


    /*
    * 获取用户名称
    * */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 200: {
                    userName=user.getUsername();
                }
                case 300: {
                    musicNameText.setText(music.getMusicname()+"("+music.getFobjectname()+music.getType()+")");
                    musicSingerText.setText("歌手："+music.getSinger());
                    fjName=music.getFobjectname();
                    getAPIData4();
                    Log.d("myapplog", "fjname"+music.getFobjectname());
                }
                case 400:{
                    musicURL = (String) msg.obj;
                }
            }
        }
    };
    public void music(){
        musicWeb.loadUrl(musicURL);
    }

    /*
    * 实现评论的写入与发送
    * */
    public void send() {

        if (!validate()) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(DiscussActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendDis(progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /*
    * 获取与写入
    * */
    public void sendDis(ProgressDialog progressDialog) throws Exception{
        client = new OkHttpClient.Builder()
                .build();

        String message=eDiscussText.getText().toString();
        int uId=Integer.valueOf(userId);
        int fjId=fId;
        int pId=0;
        int puId=0;
        String name=userName;
        Log.d("myapplog", "sendDis: "+name);

        Discuss discuss=new Discuss();
        discuss.setBody(message);
        discuss.setUsername(name);
        discuss.setUserid(uId);
        discuss.setPid(pId);
        discuss.setPuid(puId);
        discuss.setFobjectid(fjId);


        String dicussJson = GsonUtil.GsonString(discuss);

        RequestBody requestBody = RequestBody.create(JSON, dicussJson);

        Request request = new Request.Builder()
                .url(MyApplication.getURL() +"discuss/add")
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
                        Toast.makeText(DiscussActivity.this, getText(R.string.dissuccess), Toast.LENGTH_LONG).show();
                    }
                });
//                startActivity(new Intent(RegistActivity.this, LoginActivity.class));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        eDiscussText.setText("");
                    }
                });
            }
        });

    }


    /*
    * 搜索用户的资料
    * */
    private User getUserData2(){
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "user/showuser?id=" + userId)
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
                Log.d("myapplog", "user:"+user.getUsername());
                Message msg = new Message();
                msg.what = 200;
                //msg.arg1 = 111;  可以设置arg1、arg2、obj等参数，传递这些数据
                //msg.arg2 = 222; msg.obj = obj;
                mHandler.sendMessage(msg);
            }
        });
        return user;
    }

    /*
    * 获取音乐信息
    * */
    private Music getMusicData3(){
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "music/fmusic?fobjectid=" + fId)
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

                MusicBean temp = GsonUtil.GsonToBean(body, MusicBean.class);
                Log.d("myapplog", "Detail:" + temp.getData().toString());

                music = temp.getData();
                //fobject = GsonUtil.GsonToBean(temp.getData(), Fobject.class);
                Log.d("myapplog", music.getMusicname());
                Message msg = new Message();
                msg.what = 300;
                //msg.arg1 = 111;  可以设置arg1、arg2、obj等参数，传递这些数据
                //msg.arg2 = 222; msg.obj = obj;
                mHandler.sendMessage(msg);
            }
        });
        return music;
    }

    /*
     * 获取音乐接口
     * */
    private void getAPIData4(){
        Request request = new Request.Builder()
                .url("https://api.itooi.cn/music/netease/search?key=579621905&s="+fjName+"&type=song&limit=1")
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

                MusicAPIBean temp = GsonUtil.GsonToBean(body, MusicAPIBean.class);
               // Log.d("myapplog", "Detail:" + temp.getData().toString());

                musicAPI = temp.getData();
                Log.d("myapplog", "Detail:" + musicAPI.getName().toString());
                //fobject = GsonUtil.GsonToBean(temp.getData(), Fobject.class);
                Log.d("myapplog", musicAPI.getUrl());
                Message msg = new Message();
                msg.what = 400;
                msg.obj = musicAPI.getUrl();
                //msg.arg1 = 111;  可以设置arg1、arg2、obj等参数，传递这些数据
                //msg.arg2 = 222; msg.obj = obj;
                mHandler.sendMessage(msg);
            }
        });

    }




    /*
    * 输入判断
    * */
    public boolean validate() {
        boolean valid = true;

        String dis=eDiscussText.getText().toString();
        if(dis.isEmpty() ){
            eDiscussText.setError(getString(R.string.suggect));
            valid = false;
        } else {
            eDiscussText.setError(null);
        }
        return valid;
    }
}
