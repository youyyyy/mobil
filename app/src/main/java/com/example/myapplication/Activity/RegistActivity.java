package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class RegistActivity extends AppCompatActivity {

    private boolean manSelected = false;
    private boolean womanSelected = false;

    @BindView(R.id.btn_signup)
    Button signupButton;
    @BindView(R.id.signupinfo)
    TextView signupInfo;
    @BindView(R.id.input_name)
    EditText nameText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText reEnterPasswordText;
    @BindView(R.id.input_phonenum)
    EditText phoneNumText;
    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.input_type)
    EditText typeText;
    @BindView(R.id.input_csignature)
    EditText csignatureText;

    @BindView(R.id.icon_man)
    ImageView _iconMan;
    @BindView(R.id.icon_woman)
    ImageView _iconWoman;

    private OkHttpClient client = new OkHttpClient();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        new Thread(connect).start();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }



    Runnable connect = new Runnable() {
        @Override
        public void run() {
            //todo
        }
    };

    public void signup() {
        if (!validate()) {
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(RegistActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
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

        String name=nameText.getText().toString();
        String password=passwordText.getText().toString();
        String reEnterPassword=reEnterPasswordText.getText().toString();
        String phoneNum=phoneNumText.getText().toString();
        String email=emailText.getText().toString();
        String type=typeText.getText().toString();
        String csignature=csignatureText.getText().toString();


    }

    public void saveCookie(String id, Long expiresTime){
        SharedPreferences.Editor editor = getSharedPreferences("cookies",MODE_PRIVATE).edit();
        editor.putString("id",id);
        editor.putLong("expires", expiresTime);
        editor.apply();

    }


    

}
