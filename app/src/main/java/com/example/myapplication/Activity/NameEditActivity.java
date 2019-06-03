package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class NameEditActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private String userId;

    @BindView(R.id.send_nameedit)
    ImageView nameEditButton;
    @BindView(R.id.edit_name)
    EditText editNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_edit);

        ButterKnife.bind(NameEditActivity.this);
        Intent intent = getIntent();
        userId=intent.getStringExtra("edit_userid");
        Log.d("myapplog", "签名："+userId);

        nameEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    public void send() {

        if (!validate()) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(NameEditActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.please));
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendCEdit(progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
     * 获取与写入
     * */
    public void sendCEdit(ProgressDialog progressDialog) throws Exception{
        client = new OkHttpClient.Builder()
                .build();

        String name;

        name=editNameText.getText().toString();
        int uId=Integer.valueOf(userId);


        Request request = new Request.Builder()
                .url(MyApplication.getURL() +"user/modifyname?id="+uId+"&username="+name)
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
                        Toast.makeText(NameEditActivity.this, getText(R.string.nameedit), Toast.LENGTH_LONG).show();
                    }
                });
//                startActivity(new Intent(RegistActivity.this, LoginActivity.class));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editNameText.setText("");
                    }
                });
            }
        });

        Request request2 = new Request.Builder()
                .url(MyApplication.getURL() +"discuss/modifyusername?userid="+uId+"&username="+name)
                .build();

        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("myapplog", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("myapplog", "hahah:" + body);

//                startActivity(new Intent(RegistActivity.this, LoginActivity.class));

            }
        });

    }

    /*
     * 输入判断
     * */
    public boolean validate() {
        boolean valid = true;

        String dis=editNameText.getText().toString();
        if(dis.isEmpty() ){
            editNameText.setError(getString(R.string.suggect));
            valid = false;
        } else {
            editNameText.setError(null);
        }
        return valid;
    }


}

