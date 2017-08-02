//package com.huadi.android.ainiyo.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.huadi.android.ainiyo.R;
//
//import java.io.File;
//import java.util.ArrayList;
//
//public class ModeAdapter2 extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
//
//    private Context mContext;
//    private ArrayList<String> mImages;
//    private LayoutInflater mInflater;
//
//
//    public ModeAdapter2(Context context) {
//        mContext = context;
//        this.mInflater = LayoutInflater.from(mContext);
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.mode_list_row, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
//        final String image = mImages.get(position);
//        Glide.with(mContext).load(new File(image)).into(holder.ivImage);
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return mImages == null ? 0 : mImages.size();
//    }
//
//    public void refresh(ArrayList<String> images) {
//        mImages = images;
//        notifyDataSetChanged();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView ivImage;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
//        }
//    }
//}
