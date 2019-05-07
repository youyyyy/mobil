package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends Activity {

    private int fjId;
    private Bitmap bitmap;
    private String fjName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //ButterKnife.bind(this);
        RecyclerView myRecycler = (RecyclerView) findViewById(R.id.myRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        myRecycler.setLayoutManager(manager);
        myRecycler.setHasFixedSize(true);

        Intent intent = getIntent();
        fjId = intent.getIntExtra("id", 0);
        byte[] bitMapByte = intent.getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(bitMapByte, 0, bitMapByte.length);
        fjName=intent.getStringExtra("fjname");
        Log.d("myapplog", "hahah:" + fjName);



        final List<String> content = new ArrayList<>();
        for (int i = 0; i < 30; i++)
            content.add(getListString(i));


        ParallaxRecyclerAdapter<String> stringAdapter = new ParallaxRecyclerAdapter<String>(content) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<String> adapter, int i) {
                // If you're using your custom handler (as you should of course)
                // you need to cast viewHolder to it.
                ((TextView) viewHolder.itemView).setText(content.get(i));
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, final ParallaxRecyclerAdapter<String> adapter, int i) {
                // Here is where you inflate your row and pass it to the constructor of your ViewHolder
                return new SimpleViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<String> adapter) {
                // return the content of your array
                return content.size();
            }
        };



        View headView = getLayoutInflater().inflate(R.layout.details_header, myRecycler, false);
        ImageView headreImageView = headView.findViewById(R.id.imageView2);
        headreImageView.setImageBitmap(bitmap);
        TextView fjText = headView.findViewById(R.id.fjtitle);
        fjText.setText(fjName);


        stringAdapter.setParallaxHeader(headView, myRecycler);
        stringAdapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
                //TODO: implement toolbar alpha. See README for details
            }
        });
        stringAdapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View view, int i) {
                //TODO
                Toast.makeText(DetailsActivity.this, String.valueOf(i), Toast.LENGTH_LONG).show();
            }
        });
        myRecycler.setAdapter(stringAdapter);
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public String getListString(int position) {
        return position + " - android";
    }
}
