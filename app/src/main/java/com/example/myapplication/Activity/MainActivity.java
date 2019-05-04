package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ObjectAdapter;
import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.FJList;
import com.example.myapplication.Bean.Fobject;
import com.example.myapplication.Bean.FobjectList;
import com.example.myapplication.R;
import com.example.myapplication.Util.GsonUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private TextView xixi;

    private OkHttpClient client = new OkHttpClient();

    private String dateForFJ = "4";
    private List<FJList> fjLists = new ArrayList<>();
    private List<FJList> tempfjLists = new ArrayList<>();
    private ObjectAdapter objectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        xixi = findViewById(R.id.xixi);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

       //TODO
        //读取本地保存的list，把数据添加到fjLists

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        objectAdapter = new ObjectAdapter(fjLists);
        recyclerView.setAdapter(objectAdapter);


        View headerView = navigationView.getHeaderView(0);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
                startActivity(new Intent(MainActivity.this, SelfActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_message) {

        } else if (id == R.id.nav_Monday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "2";
           updateData();

        } else if (id == R.id.nav_Tuesday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "3";
            updateData();

        } else if (id == R.id.nav_Wednesday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "4";
            updateData();

        } else if (id == R.id.nav_Thursday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "5";
            updateData();

        }else if (id == R.id.nav_Friday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "6";

        }else if (id == R.id.nav_Saturday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "7";
            updateData();

        }else if (id == R.id.nav_Sunday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "1";
            updateData();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<FJList> getNewList() {
        return tempfjLists;
    }

    private void updateData() {
        initList();
    }

    private void initList () {

        //TODO
        //开始动画
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "fobject/bytime?updatetime=" + dateForFJ)
                .build();

        Log.d("myapplog", "URL: " + MyApplication.getURL() + "fobject/bytime?updatetime=" + dateForFJ);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("myapplog", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("myapplog", "hahah:" + body);

                FobjectList temp= GsonUtil.GsonToBean(body, FobjectList.class);
                List<Fobject> fobjectList = temp.getData();
                tempfjLists.clear();
                for (Fobject f: fobjectList) {
                    tempfjLists.add(getImage(f));
                }
                fjLists.clear();
                fjLists.addAll(getNewList());

                //TODO
                //保存这个list到本地

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO
                        //停止动画
                        objectAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private FJList getImage(Fobject fobject){
        FJList temp = new FJList();

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(MyApplication.getURL() + "fobject/get?id=" + fobject.getId())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            InputStream inputStream = response.body().byteStream();
            Bitmap tempPic = BitmapFactory.decodeStream(inputStream);
            temp.setImage(tempPic);
            temp.setName(fobject.getTitle());
            //tempfjLists.add(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
