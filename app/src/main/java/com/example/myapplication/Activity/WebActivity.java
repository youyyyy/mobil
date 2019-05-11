package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class WebActivity extends AppCompatActivity {

    private int fjId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        fjId=intent.getIntExtra("del_fjId",0);
        Log.d("myapplog", "fjid获取 "+fjId);
    }
}
