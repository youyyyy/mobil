package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class SelfActivity extends AppCompatActivity {

    private String selfId;
    private User user;

    private OkHttpClient client = new OkHttpClient();

    @BindView(R.id.self_id)
    TextView idText;
    @BindView(R.id.self_levels)
    TextView levelsText;
    @BindView(R.id.self_name)
    TextView nameText;
    @BindView(R.id.self_csignature)
    TextView csignatureText;
    @BindView(R.id.self_gender)
    TextView genderText;
    @BindView(R.id.self_type)
    TextView typeText;
//    @BindView(R.id.user_phone)
//    TextView phoneText;
    @BindView(R.id.self_email)
    TextView emailText;
    @BindView(R.id.self_edit_csignature)
    ImageView edtiCsignature;
    @BindView(R.id.self_edit_name)
    ImageView editName;
    @BindView(R.id.self_edit_type)
    ImageView editType;
    @BindView(R.id.self_edit_gender)
    ImageView editGender;
    @BindView(R.id.self_name_card)
    CardView nameCard;
    @BindView(R.id.self_gender_card)
    CardView genderCard;
    @BindView(R.id.self_type_card)
    CardView typeCard;
    @BindView(R.id.self_cancel)
    ImageView cancelImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);
        ButterKnife.bind(SelfActivity.this);
        Intent intent = getIntent();
        selfId=intent.getStringExtra("self_uid");
        Log.d("myapplog", "user: " + selfId);

        edtiCsignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelfActivity.this, CEditActivity.class);
                String userId=getPreference(v.getContext(), "id");
                intent.putExtra("edit_userid",userId);
                startActivity(intent);
                //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
            }
        });

        nameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelfActivity.this, NameEditActivity.class);
                String userId=getPreference(v.getContext(), "id");
                intent.putExtra("edit_userid",userId);
                startActivity(intent);
                //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
            }
        });

        typeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelfActivity.this, TypeEditActivity.class);
                String userId=getPreference(v.getContext(), "id");
                intent.putExtra("edit_userid",userId);
                startActivity(intent);
                //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
            }
        });

        genderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelfActivity.this, GenderEditActivity.class);
                String userId=getPreference(v.getContext(), "id");
                intent.putExtra("edit_userid",userId);
                startActivity(intent);
                //startActivity(new Intent(DetailsActivity.this, DiscussActivity.class));
            }
        });

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor sharedPreferences = getSharedPreferences("cookies", MODE_PRIVATE).edit();
                Long date =System.currentTimeMillis() ;
                sharedPreferences.putLong("expires", date);
                sharedPreferences.apply();
                Intent intent=new Intent(SelfActivity.this,SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
               // startActivity(new Intent(SelfActivity.this, LoginActivity.class));
            }
        });

        getData();

    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 200: {
                    idText.setText("ID："+selfId);
                    nameText.setText("昵称："+user.getUsername());
                    levelsText.setText("等级："+user.getLevels());
                    if (user.getGender()==1){
                        genderText.setText("性别："+"男");
                    } else if (user.getGender()==2){
                        genderText.setText("性别："+"女");
                    } else if(user.getGender()==3){
                        genderText.setText("性别："+"保密");
                    }
                    if(user.getType().equals("null")) {
                        typeText.setText("Ta还没填写哟~");
                    } else {
                        typeText.setText("Ta的喜好：" + user.getType());
                    }
                    // phoneText.setText("电话："+user.getPhonenum());
                    emailText.setText("邮箱："+user.getEmail());
                    if(user.getCsignature().equals("null"))
                        csignatureText.setText("这个人很懒，什么也没留下~");
                    else
                    csignatureText.setText(user.getCsignature());

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

    public static String getPreference(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        return preference.getString(key, "");
    }

}
