package com.example.myapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemClock.sleep(1500);
        loginCheck();

    }

    private void loginCheck() {
        SharedPreferences sharedPreferences = getSharedPreferences("cookies", MODE_PRIVATE);
        Long date = sharedPreferences.getLong("expires", 1);
        if (date - System.currentTimeMillis() < 0 ) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } else  {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

        }
    }


}
