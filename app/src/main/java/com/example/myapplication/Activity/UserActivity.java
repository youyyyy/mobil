package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application.MyApplication;
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
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private int userId;
    private User user;

    @BindView(R.id.user_id)
    TextView idText;
    @BindView(R.id.user_name)
    TextView nameText;
    @BindView(R.id.user_gender)
    TextView genderText;
    @BindView(R.id.user_type)
    TextView typeText;
//    @BindView(R.id.user_phone)
//    TextView phoneText;
    @BindView(R.id.user_email)
    TextView emailText;
    @BindView(R.id.user_csignature)
    TextView csignatureText;
    @BindView(R.id.user_levels)
    TextView levelsText;
    @BindView(R.id.head_sculpture)
    ImageView sculptureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(UserActivity.this);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_userid",0);
        Log.d("myapplog", "用户资料Id: " + userId);

        MyApplication.networkCheck();

        getData();
    }



    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 200: {
                    idText.setText("ID："+userId);
                    nameText.setText("昵称："+user.getUsername());
                    levelsText.setText("等级："+user.getLevels());
                    if (user.getGender()==1){
                        genderText.setText("性别："+"男");
                        sculptureImage.setImageResource(R.drawable.ic_sex_man);
                    } else if (user.getGender()==2){
                        genderText.setText("性别："+"女");
                        sculptureImage.setImageResource(R.drawable.ic_sex_woman);
                    } else if(user.getGender()==3){
                        genderText.setText("性别："+"保密");
                        sculptureImage.setImageResource(R.drawable.ic_sex_no);
                    }
                    if(user.getType()=="null") {
                        typeText.setText("Ta还没填写哟~");
                    } else {
                        typeText.setText("Ta的喜好：" + user.getType());
                    }
                   // phoneText.setText("电话："+user.getPhonenum());
                    emailText.setText("邮箱："+user.getEmail());

                    if(user.getCsignature()=="null"){
                        csignatureText.setText("这个人很懒，什么也没留下~");
                    }
                    else {
                        csignatureText.setText(user.getCsignature());
                    }
                }
            }
        };
    };


    private User getData(){
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "user/showuser?id=" + userId)
                .build();

        Log.d("myapplog","request url: " + MyApplication.getURL() + "user/showuser?id=" + userId);

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
}
