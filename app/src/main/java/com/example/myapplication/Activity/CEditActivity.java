package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CEditActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

   // private User user;

    private String userId;

    @BindView(R.id.cedit_csignature)
    EditText editCsignatureText;
    @BindView(R.id.send_cedit)
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cedit);
        ButterKnife.bind(CEditActivity.this);
        Intent intent = getIntent();
        userId=intent.getStringExtra("edit_userid");
        Log.d("myapplog", "签名："+userId);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

    }

    public void send() {

//        if (!validate()) {
//            return;
//        }
        final ProgressDialog progressDialog = new ProgressDialog(CEditActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.donghua));
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

        String csignature;
        if(editCsignatureText.getText().toString().isEmpty())
            csignature="这个人很懒，什么也没留下~";
        else
            csignature=editCsignatureText.getText().toString();
        int uId=Integer.valueOf(userId);




        Request request = new Request.Builder()
                .url(MyApplication.getURL() +"user/modifycsignature?id="+uId+"&csignature="+csignature)
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
                        Toast.makeText(CEditActivity.this, getText(R.string.cedit), Toast.LENGTH_LONG).show();
                    }
                });
//                startActivity(new Intent(RegistActivity.this, LoginActivity.class));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editCsignatureText.setText("");
                    }
                });
            }
        });

    }

    /*
     * 输入判断
     * */
//    public boolean validate() {
//        boolean valid = true;
//
//        String dis=editCsignatureText.getText().toString();
//        if(dis.isEmpty() ){
//            editCsignatureText.setError(getString(R.string.suggect));
//            valid = false;
//        } else {
//            editCsignatureText.setError(null);
//        }
//        return valid;
//    }
}
