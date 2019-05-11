package com.example.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.User;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistActivity extends AppCompatActivity {

    private boolean manSelected = false;
    private boolean womanSelected = false;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private  LayoutInflater mInflater;
    private String[] labels;


//    @BindView(R.id.genderview)
//    TextView genderView;
    @BindView(R.id.textview)
    TextView textView;
    @BindView(R.id.btn_signup)
    androidx.appcompat.widget.AppCompatButton signupButton;
    @BindView(R.id.signupinfo)
    TextView signupInfo;
    @BindView(R.id.input_name)
    EditText nameText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText reEnterPasswordText;
    @BindView(R.id.input_phoneNum)
    EditText phoneNumText;
    @BindView(R.id.input_email)
    EditText emailText;
//    @BindView(R.id.input_type)
//    EditText typeText;
    @BindView(R.id.input_csignature)
    EditText csignatureText;

    @BindView(R.id.icon_man)
    ImageView _iconMan;
    @BindView(R.id.icon_woman)
    ImageView _iconWoman;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowLayout;


    private OkHttpClient client = new OkHttpClient();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        mInflater = LayoutInflater.from(RegistActivity.this);

        labels = initLabel();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        _iconMan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                _iconMan.setImageResource(R.drawable.ic_launcher_background);
                _iconWoman.setImageResource(R.drawable.ic_launcher_foreground);
                manSelected = true;
                womanSelected = false;
            }
        });

        _iconWoman.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                _iconMan.setImageResource(R.drawable.ic_launcher_foreground);
                _iconWoman.setImageResource(R.drawable.ic_launcher_background);
                manSelected = false;
                womanSelected = true;
            }
        });

    }

    private String[] initLabel () {
        String[] mVals = new String[]
                {"搞笑", "科幻", "动作", "热血", "青春", "恐怖","恋爱","乙女","励志","冒险","奇幻","轻小说","励志","科幻","泡面番"};
        flowLayout.setMaxSelectCount(3);
        flowLayout.setAdapter(new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.label,
                        flowLayout, false);
                tv.setText(s);
                return tv;
            }


        });
        return  mVals;
    }

    public void signUp() {

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
                .build();

        int gender;
        if(manSelected=true)
            gender=1;
        else if (womanSelected=true)
            gender=2;
        else
            gender=3;

        String username=nameText.getText().toString();
        String password=passwordText.getText().toString();
        String reEnterPassword=reEnterPasswordText.getText().toString();
        String phoneNum=phoneNumText.getText().toString();
        String email=emailText.getText().toString();
        String csignature=csignatureText.getText().toString();
        String type = "";
        String levels="1";

        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhonenum(phoneNum);
        user.setEmail(email);
        user.setCsignature(csignature);
        user.setLevels(levels);
        user.setGender(gender);

        for (int x: flowLayout.getSelectedList()) {
            type += labels[x] + ",";
        }
        user.setType(type);

        if (user.getCsignature().isEmpty() || user.getCsignature() == null) {
            user.setCsignature("这个人很懒，什么也没留下~");
        }

        if (user.getType().isEmpty() || user.getType() == null) {
            user.setType("null");
        }

        String userJson = GsonUtil.GsonString(user);
        Log.d("myapplog", "1JSON: " + userJson);


        RequestBody requestBody = RequestBody.create(JSON, userJson);

        Request request = new Request.Builder()
                .url(MyApplication.getURL() +"user/regist")
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
                        Toast.makeText(RegistActivity.this, getText(R.string.signup_success), Toast.LENGTH_LONG).show();
                    }
                });
                startActivity(new Intent(RegistActivity.this, LoginActivity.class));
                finish();
            }
        });





    }

    public void saveCookie(String id, Long expiresTime){
        SharedPreferences.Editor editor = getSharedPreferences("cookies",MODE_PRIVATE).edit();
        editor.putString("id",id);
        editor.putLong("expires", expiresTime);
        editor.apply();

    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();
        String phoneNum=phoneNumText.getText().toString();
        String email=emailText.getText().toString();

        if (name.isEmpty() || name.length() < 3 || name.contains(" ")) {
            nameText.setError(getString(R.string.nameLength));
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            passwordText.setError(getString(R.string.passwordLength));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError(getString(R.string.passwordMtch));
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        if (phoneNum.isEmpty()) {
            phoneNumText.setError(getString(R.string.phoneNum));
            valid = false;
        }else {
            phoneNumText.setError(null);
        }

        if (email.isEmpty()) {
            emailText.setError(getString(R.string.email));
            valid = false;
        }else {
            emailText.setError(null);
        }

        return valid;
    }



}
