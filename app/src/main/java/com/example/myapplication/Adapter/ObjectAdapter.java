package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Bean.FJList;
import com.example.myapplication.R;

import java.util.List;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder>{
    private List<FJList> mObjectList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView objectImage;
        TextView objectName;

        public ViewHolder(View view) {
            super(view);
            objectImage = (ImageView) view.findViewById(R.id.object_image);
            objectName = (TextView) view.findViewById(R.id.object_text);
        }
    }

    public ObjectAdapter(List<FJList>  objectList) {
        mObjectList = objectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.object_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FJList fj = mObjectList.get(position);
        holder.objectImage.setImageBitmap(fj.getImage());
        holder.objectName.setText(fj.getName());
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }
}
