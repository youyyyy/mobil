package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.normalReturnBean;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;
import com.example.myapplication.Util.MD5Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
用户登录
 */
public class LoginActivity extends AppCompatActivity {

    //注册按钮
    @BindView(R.id.input_phonenum)
    EditText phoneNumdText;
    @BindView(R.id.input_password)
    EditText passWordText;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.logininfo)
    TextView loginInfo;
    @BindView(R.id.btn_regist)
    Button registButton;
    @BindView(R.id.registinfo)
    TextView registInfo;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        new Thread(connect).start();

        //登陆按钮响应事件
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login();
            }
        });

        //注册按钮响应事件
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));

            }
        });
    }


    Runnable connect = new Runnable() {
        @Override
        public void run() {
           //todo
        }
    };

    /*
    * 加特技
    * */
    public void login() {
        if (!validate()) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.please));
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runHttp(progressDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
    * 保存cookie
    * */
    public void runHttp (ProgressDialog progressDialog) throws Exception{

        client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        Log.d("myapplog", "cookies url: " + url.toString());
                        for (Cookie cookie : cookies) {
                            Log.d("myapplog", "cookies: " + cookie.toString());
                            saveCookie(cookie.value().toString(), cookie.expiresAt());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                            }
                        });
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        ArrayList<Cookie> cookies = new ArrayList<>();
                        Cookie cookie = new Cookie.Builder()
                                .hostOnlyDomain(url.host())
                                .name("login").value("123")
                                .build();
                        cookies.add(cookie);
                        return cookies;
                    }
                })
                .build();

        String username = phoneNumdText.getText().toString();
        String password1 = passWordText.getText().toString();
        String password= MD5Util.md5Password(password1);

        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "user/login?phonenum=" + username + "&password=" + password)
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

                normalReturnBean temp = GsonUtil.GsonToBean(body, normalReturnBean.class);
               // User user = GsonUtil.GsonToBean(temp.getData().toString(), User.class);
               // Log.d("myapplog", user.getPhonenum());
                if(temp.getCode()==404){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.cancel();
                            Toast.makeText(LoginActivity.this, R.string.loginError, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(LoginActivity.this, R.string.loginError, Toast.LENGTH_SHORT).show();

                }else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }


    /*
    * 存cookie方法
    * */
    public void saveCookie(String id, Long expiresTime){
        SharedPreferences.Editor editor = getSharedPreferences("cookies",MODE_PRIVATE).edit();
        editor.putString("id",id);
        editor.putLong("expires", expiresTime);
        editor.apply();

    }


    /*
    * 判断输入内容
    * */
    public boolean validate() {
        boolean valid = true;

        String phonenum = phoneNumdText.getText().toString();
        String password = passWordText.getText().toString();

        if (phonenum.isEmpty() || phonenum.contains(" ")) {
            phoneNumdText.setError(getString(R.string.input_phone_hint));
            valid = false;
        } else {
            phoneNumdText.setError(null);
        }

        if (password.length() < 4 || password.length() > 10) {
            //_passwordText.setError("between 4 and 10 alphanumeric characters");
            passWordText.setError(getString(R.string.password_err_hint));
            valid = false;
        } else {
            passWordText.setError(null);
        }

        if (password.isEmpty()) {
            //_passwordText.setError("between 4 and 10 alphanumeric characters");
            passWordText.setError(getString(R.string.input_pwd_hint));
            valid = false;
        } else {
            passWordText.setError(null);
        }


        return valid;
    }


}
