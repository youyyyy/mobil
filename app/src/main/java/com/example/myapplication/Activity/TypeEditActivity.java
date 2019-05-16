package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TypeEditActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();

    private String userId;
    private String[] labels;
    private LayoutInflater mInflater;

    @BindView(R.id.send_typeedit)
    Button typeEditButton;
    @BindView(R.id.type_flowlayout)
    TagFlowLayout typeFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_edit);

        ButterKnife.bind(TypeEditActivity.this);
        mInflater = LayoutInflater.from(TypeEditActivity.this);
        Intent intent = getIntent();
        userId = intent.getStringExtra("edit_userid");

        labels = initLabel();

        typeEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    private String[] initLabel() {
        String[] mVals = new String[]
                {"搞笑", "动作", "热血", "青春", "恐怖", "恋爱", "乙女", "励志", "冒险", "奇幻", "轻小说", "励志", "科幻", "泡面番", "运动", "百合", "科幻", "耽美"};
        typeFlowLayout.setMaxSelectCount(3);
        typeFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.label,
                        typeFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        return mVals;
    }


    public void send() {

//        if (!validate()) {
//            return;
//        }
        final ProgressDialog progressDialog = new ProgressDialog(TypeEditActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.donghua));
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendTypeEdit(progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendTypeEdit(ProgressDialog progressDialog) throws Exception {
        client = new OkHttpClient.Builder()
                .build();

        String type;

        type = "";
        for (int x : typeFlowLayout.getSelectedList()) {
            type += labels[x] + "、";
        }
        int uId = Integer.valueOf(userId);


        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "user/modifytype?id=" + uId + "&type=" + type)
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
                        Toast.makeText(TypeEditActivity.this, getText(R.string.nameedit), Toast.LENGTH_LONG).show();
                    }
                });
//                startActivity(new Intent(RegistActivity.this, LoginActivity.class));
            }
        });


    }
}
