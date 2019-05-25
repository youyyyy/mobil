package com.example.myapplication.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.DiscussDetailsActivity;
import com.example.myapplication.Application.MyApplication;
import com.example.myapplication.Bean.Collect;
import com.example.myapplication.R;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelfCollectAdapter extends RecyclerView.Adapter<SelfCollectAdapter.ViewHolder> {

    private OkHttpClient client = new OkHttpClient();

    private int position;
    private int fjid;
    private List<Collect> mCollect;
    private Collect collect;
    private String URL;
    private int collectId;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View objectView;
        TextView collectTitle;
        TextView updatetime;
        ImageView collectdelete;


        public ViewHolder(View view) {
            super(view);
            objectView = view;
            collectTitle = (TextView) view.findViewById(R.id.self_collect_title);
            updatetime = (TextView) view.findViewById(R.id.collect_updatetime);
            collectdelete = (ImageView) view.findViewById(R.id.collect_delete);
        }
    }

    public SelfCollectAdapter(List<Collect> collectList) { mCollect = collectList; }

    @Override
    public SelfCollectAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collect_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.collectTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                Collect collect = mCollect.get(position);
                fjid = (collect.getFjid());
                Log.d("myapplog", "sendfjid:" + fjid);
                Intent intent = new Intent(view.getContext(), DiscussDetailsActivity.class);
                intent.putExtra("fj_Id", fjid);

                view.getContext().startActivity(intent);
                //Object object = mObjectList.get(position);
            }
        });

        holder.collectdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = holder.getAdapterPosition();
                Collect collect = mCollect.get(position);
                sendDelete(collect.getId());
                Toast.makeText(parent.getContext(),parent.getContext().getText(R.string.deletecollect), Toast.LENGTH_LONG).show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder( SelfCollectAdapter.ViewHolder holder, int position) {
        Collect col=mCollect.get(position);
        holder.collectTitle.setText(col.getFjname());
        switch (col.getUpdatetime()) {
            case 1:
                holder.updatetime.setText("周日更新");
                break;
            case 2:
                holder.updatetime.setText("周一更新");
                break;
            case 3:
                holder.updatetime.setText("周二更新");
                break;
            case 4:
                holder.updatetime.setText("周三更新");
                break;
            case 5:
                holder.updatetime.setText("周四更新");
                break;
            case 6:
                holder.updatetime.setText("周五更新");
                break;
            case 7:
                holder.updatetime.setText("周六更新");
                break;
            case 0:
                holder.updatetime.setText("已完结");
                break;
                default:
                    break;
        }
        collectId=col.getId();

    }

    @Override
    public int getItemCount() {
        return mCollect.size();
    }

    public void sendDelete(int collect_Id) {
        client = new OkHttpClient.Builder()
                .build();


        URL = "collect/delete?id=" + collect_Id;

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
