package com.huadi.android.ainiyo.adapter;

import android.widget.ImageView;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;


import java.util.List;

/**
 * Created by 45990 on 2017/8/13.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHodler> {

    private Context mContext;
    private List<String> mList;

    public MyAdapter(Context context, List<String> data) {
        mContext = context;
        mList=data;
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_image_item,parent,false);

        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {

        Glide.with(mContext).load(mList.get(position)).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder{

        private ImageView mImageView;

        public ViewHodler(View itemView) {
            super(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.im_photo);
        }
    }
}