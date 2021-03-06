package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenderEditActivity extends AppCompatActivity {

    private boolean manSelected = false;
    private boolean womanSelected = false;
    private boolean noSelected = false;

    private OkHttpClient client = new OkHttpClient();

    private String userId;
    private int editgender=3;

    @BindView(R.id.edit_icon_man)
    ImageView iconMan;
    @BindView(R.id.edit_icon_woman)
    ImageView iconWoman;
    @BindView(R.id.send_genderedit)
    ImageView genderEditButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_edit);

        ButterKnife.bind(GenderEditActivity.this);
        Intent intent = getIntent();
        userId = intent.getStringExtra("edit_userid");


        genderEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        iconMan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                iconMan.setImageResource(R.drawable.ic_man);
                iconWoman.setImageResource(R.drawable.ic_woman_select);
                manSelected = true;
                womanSelected = false;
                editgender=1;
            }
        });

        iconWoman.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                iconMan.setImageResource(R.drawable.ic_man_select);
                iconWoman.setImageResource(R.drawable.ic_woman);
                manSelected = false;
                womanSelected = true;
                editgender=2;
            }
        });

        MyApplication.networkCheck();

    }

    public void send() {

//        if (!validate()) {
//            return;
//        }
        final ProgressDialog progressDialog = new ProgressDialog(GenderEditActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.please));
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendGenderEdit(progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendGenderEdit(ProgressDialog progressDialog) throws Exception {
        client = new OkHttpClient.Builder()
                .build();



        int uId = Integer.valueOf(userId);


        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "user/modifygender?id=" + uId + "&gender=" + editgender)
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
                        Toast.makeText(GenderEditActivity.this, getText(R.string.nameedit), Toast.LENGTH_LONG).show();
                    }
                });
//                startActivity(new Intent(RegistActivity.this, LoginActivity.class));
            }
        });


    }
}
