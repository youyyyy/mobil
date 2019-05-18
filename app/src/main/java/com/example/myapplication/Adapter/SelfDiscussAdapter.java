package com.example.myapplication.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.DiscussDetailsActivity;
import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.DiscussReturn;
import com.example.myapplication.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelfDiscussAdapter extends RecyclerView.Adapter<SelfDiscussAdapter.ViewHolder> {

    private OkHttpClient client = new OkHttpClient();

    private int position;
    private int fjid;
    private List<DiscussReturn> mDiscussReturn;
    private DiscussReturn discussReturn;
    private String URL;
    private int disId;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View objectView;
        TextView disBody;
        TextView disTime;
        TextView disDelete;


        public ViewHolder(View view) {
            super(view);
            objectView = view;
            disBody = (TextView) view.findViewById(R.id.self_dicuss_body);
            disTime = (TextView) view.findViewById(R.id.self_dicuss_time);
            disDelete = (TextView) view.findViewById(R.id.self_discuss_delete);
        }
    }

    public SelfDiscussAdapter(List<DiscussReturn> discussList) {
        mDiscussReturn = discussList;
    }


    @Override
    public SelfDiscussAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.self_discuss_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.disBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                DiscussReturn discussReturn = mDiscussReturn.get(position);
                fjid = (discussReturn.getFobjectid());
                Log.d("myapplog", "sendfjid:" + fjid);
                Intent intent = new Intent(view.getContext(), DiscussDetailsActivity.class);
                intent.putExtra("fj_Id", fjid);

                view.getContext().startActivity(intent);
                //Object object = mObjectList.get(position);
            }
        });

        holder.disDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                DiscussReturn discussReturn = mDiscussReturn.get(position);
                sendDelete(discussReturn.getId());
                Toast.makeText(parent.getContext(),parent.getContext().getText(R.string.nameedit), Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(SelfDiscussAdapter.ViewHolder holder, int position) {
        DiscussReturn dis = mDiscussReturn.get(position);
        Date stime = dis.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH时");
        String sTime = format1.format(stime);
        holder.disBody.setText(dis.getBody());
        holder.disTime.setText(sTime);
        disId = dis.getId();
    }

    @Override
    public int getItemCount() {
        return mDiscussReturn.size();
    }




    public void sendDelete(int dis_Id) {
        client = new OkHttpClient.Builder()
                .build();


        URL = "discuss/delete/" + dis_Id;

        Request request = new Request.Builder()
                .url(MyApplication.getURL() + URL)
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

//                startActivity(new Intent(RegistActivity.this, LoginActivity.class));
            }
        });

    }
}
