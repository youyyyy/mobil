package com.example.myapplication.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.DetailsActivity;
import com.example.myapplication.Bean.FJList;
import com.example.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SelfObjectAdapter extends RecyclerView.Adapter<SelfObjectAdapter.ViewHolder>{
    private List<FJList> mObjectList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View objectView;
        ImageView objectImage;
        TextView objectName;

        public ViewHolder(View view) {
            super(view);
            objectView=view;
            objectImage = (ImageView) view.findViewById(R.id.self_object_image);
            objectName = (TextView) view.findViewById(R.id.self_object_name);
        }
    }

    public SelfObjectAdapter(List<FJList>  objectList) {
        mObjectList = objectList;
    }

    @Override
    public SelfObjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.self_object_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.objectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FJList object = mObjectList.get(position);
                int id = (object.getId());
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra("fj_id", id);

                Bitmap bitmap = object.getImage();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bitMapByte = byteArrayOutputStream.toByteArray();
                intent.putExtra("bitmap", bitMapByte);

                String name=object.getName();
                intent.putExtra("fj_name",name);

                view.getContext().startActivity(intent);
                //Object object = mObjectList.get(position);
            }
        });
        holder.objectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FJList object = mObjectList.get(position);
                int id = (object.getId());
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra("fj_id", id);

                Bitmap bitmap = object.getImage();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] bitMapByte = byteArrayOutputStream.toByteArray();
                intent.putExtra("bitmap", bitMapByte);

                String name=object.getName();
                intent.putExtra("fj_name",name);

                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SelfObjectAdapter.ViewHolder holder, int position) {
        FJList fj = mObjectList.get(position);
        holder.objectImage.setImageBitmap(fj.getImage());
        holder.objectName.setText(fj.getName());
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }
}
