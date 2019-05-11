package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
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

    private String dateForFJ = getDateForFJ();
//    final Calendar c = Calendar.getInstance();
//    c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//    dateForFJ = c.get(Calendar.DAY_OF_WEEK);
    private List<FJList> fjLists = new ArrayList<>();
    private List<FJList> tempfjLists = new ArrayList<>();
    private ObjectAdapter objectAdapter;
    private String userType;


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

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (!MyApplication.getHaveRun()) {
//                    //TODO
//
//                    //判断网络连接是否正常。同步
//                    //1.
//                    if (false) {
//                        //获取当前日期
//                        dateForFJ = getDateForFJ();
//
//                        //请求数据，更新list
//                        updateData();
//                    } else {
//                        //2.
//                        //读取已经保存的list
//                        String peopleListJson =getSharedPreferences("spNewList",MODE_PRIVATE).getString("KEY_getNewList","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
//                        if(peopleListJson!="")  //防空判断
//                        {
//                            //Gson gson = new Gson();
//                            //fjLists = gson.fromJson(peopleListJson, new TypeToken<List<FJList>>() {}.getType()); //将json字符串转换成List集合
//                            List<FJList> temp = GsonUtil.GsonToList(peopleListJson, FJList.class);
//                            //显示
//                            fjLists.clear();
//                            fjLists.addAll(temp);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    objectAdapter.notifyDataSetChanged();
//                                }
//                            });
//                        }
//
//                    }
//                    MyApplication.setHaveRun(true);
//                }
//            }
//        }).start();
//
//        View headerView = navigationView.getHeaderView(0);
//
        getDateForFJ();
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

    //控件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent tempIntent = new Intent(MainActivity.this, SelfActivity.class);
            tempIntent.putExtra("self_uid", getUid(this));
            startActivity(tempIntent);
            // Handle the camera action
        } else if (id == R.id.nav_message) {

        } else if(id==R.id.nav_recommend){
            Intent tempIntent = new Intent(MainActivity.this, SelfActivity.class);
            tempIntent.putExtra("self_uid", getUid(this));
            startActivity(tempIntent);

        }else if (id == R.id.nav_Monday) {
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
            updateData();

        }else if (id == R.id.nav_Saturday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "7";
            updateData();

        }else if (id == R.id.nav_Sunday) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "1";
            updateData();
        }
        else if (id == R.id.nav_Finish) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "0";
            updateData();
        }
        else if (id == R.id.nav_All) {
            Toast.makeText(MainActivity.this, R.string.Developing, Toast.LENGTH_SHORT).show();
            dateForFJ = "8";
            updateData();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private String getDateForFJ() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return String.valueOf(c.get(Calendar.DAY_OF_WEEK));
    }

    private List<FJList> getNewList() {
        return tempfjLists;
    }

    private void updateData() {
        initList();
    }

    private void initList () {
        String URL;
        if(dateForFJ=="8")
            URL=MyApplication.getURL() +"fobject/selectall";
        else
            URL=MyApplication.getURL() + "fobject/bytime?updatetime=" + dateForFJ;

        //TODO
        //开始动画
        Request request = new Request.Builder()
                .url(URL)
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
               // SharedPreferences sp = mBaseActivity.getSharedPreferences("getNewList", MODE_PRIVATE);//创建sp对象
                Gson gson = new Gson();
                String jsonStr=gson.toJson(fjLists); //将List转换成Json
                SharedPreferences.Editor editor =  getSharedPreferences("spNewList",MODE_PRIVATE).edit();
                editor.putString("KEY_getNewList", jsonStr) ; //存入json串
                editor.commit() ;  //提交

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
            temp.setId(fobject.getId());
            //tempfjLists.add(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getUid(Context context) {
        SharedPreferences preference = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        return preference.getString("id", "");
    }
}
