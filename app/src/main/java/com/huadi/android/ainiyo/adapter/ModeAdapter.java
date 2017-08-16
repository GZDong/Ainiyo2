package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.ModeAddingActivity;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.entity.ModeLocalData;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengsam on 17-7-26.
 */

public class ModeAdapter extends BaseAdapter {

    private List<ModeLocalData> mList;
    private ImageAdapter mAdapter;
    private Context mContext;
    private OnPicHeadItemClickListener mPicHeadItemClickListener;

    public ModeAdapter(Context mContext, List<ModeLocalData> list) {
        this.mContext = mContext;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0:mList.size();
    }

    @Override
    public ModeLocalData getItem(int position) {
        return (mList == null || position>=mList.size())?null:mList.get(position);
    }

    public String getPhoItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position).getMi().getImgUrlforContent().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.mode_list_row, null);
            holder=new ViewHolder();
            ViewUtils.inject(holder, convertView);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        ModeInfo modeInfo = mList.get(position).getMi();
        if(modeInfo.getName()!=null){
            holder.mode_username.setText(modeInfo.getName());
        }
        if(modeInfo.getContent()!=null) {
            holder.mode_content.setText(modeInfo.getContent());
        }
        if (modeInfo.getImgUrlforHead()!=null)
        {

        }
        if (modeInfo.getImgUrlforContent()!=null)
        {
//            if(modeInfo.getImgUrlforContent().size()==1) {
                final String image = modeInfo.getImgUrlforContent().get(0);
            Glide.with(parent.getContext()).load(image).into(holder.pic_content);

                // Glide.with(parent.getContext()).load("http://120.24.168.102:8080/getalumb?sessionid=5ca6b5f4b438030f123fb149ff19fd8769365789").skipMemoryCache(false).into(holder.pic_content);
//            }else{
//                mAdapter.refresh(modeInfo.getImgUrlforContent());
//            }
        }
        holder.pic_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPicHeadItemClickListener.OnPicHeadItemClick(position);
            }
        });

        return convertView;
    }

    public void setmPicHeadItemClickListener(OnPicHeadItemClickListener listener) {
        mPicHeadItemClickListener = listener;
    }

    class ViewHolder{

        @ViewInject(R.id.mode_pic_head)
        ImageView pic_head;
        @ViewInject(R.id.mode_pic_content)
        ImageView pic_content;
        @ViewInject(R.id.mode_username)
        TextView mode_username;
        @ViewInject(R.id.mode_content)
        TextView mode_content;

    }

    public interface OnPicHeadItemClickListener {
        void OnPicHeadItemClick(int positon);
    }
}
