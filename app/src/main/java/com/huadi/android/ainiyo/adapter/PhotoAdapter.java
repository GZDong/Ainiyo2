package com.huadi.android.ainiyo.adapter;

import android.content.Intent;
import android.widget.ImageView;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.BigImageActivity;


import java.util.List;

/**
 * Created by 45990 on 2017/8/13.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        View img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.im_photo);
        }
    }

    public PhotoAdapter(Context context, List<String> data) {
        mContext = context;
        mList=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_image_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Glide.with(mContext).load(mList.get(position)).into(holder.mImageView);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String url = mList.get(position);
                Intent intent = new Intent(mContext, BigImageActivity.class);
                intent.putExtra("URL", url);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}