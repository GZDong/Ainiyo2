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
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.entity.ModeLocalData;
import com.huadi.android.ainiyo.entity.UserInfoLab;
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
        String date = mList.get(position).getDate();
        if (date != null) {
            holder.mode_date.setText(date.substring(0, 10));
        }


        String mode_me_user_id = String.valueOf(mList.get(position).getUserid());
        if (UserInfoLab.get(mContext).getUserInfo().getId() != null && mode_me_user_id != null) {
            if (mode_me_user_id.equals(UserInfoLab.get(mContext).getUserInfo().getId())) {
                //Toast.makeText(mContext,"mymood",Toast.LENGTH_SHORT).show();
                //Log.i("imagehead", UserInfoLab.get(mContext).getUserInfo().getPicUrl());
                holder.mode_username.setText(UserInfoLab.get(mContext).getUserInfo().getUsername());
                Glide.with(mContext).load(UserInfoLab.get(mContext).getUserInfo().getPicUrl()).into(holder.pic_head);
            } else {
                //Toast.makeText(mContext,"myid: "+String.valueOf(modeInfo.getId())+"  myLabid: "+String.valueOf(UserInfoLab.get(mContext).getUserInfo().getId()),Toast.LENGTH_SHORT).show();
                holder.mode_username.setText(FriendsLab.get(mContext).findNameById(mode_me_user_id));
                Glide.with(mContext).load(FriendsLab.get(mContext).findUrlById(mode_me_user_id)).placeholder(R.drawable.left_image).into(holder.pic_head);
            }
        }
//            //用户姓名：
//            UserInfoLab.get(mContext).getUserInfo().getUsername();
//            //用户Id：
//            UserInfoLab.get(mContext).getUserInfo().getId();

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
        @ViewInject(R.id.tv_mode_comment_num)
        TextView tv_mode_comment_num;
        @ViewInject(R.id.mode_date)
        TextView mode_date;

    }

    public interface OnPicHeadItemClickListener {
        void OnPicHeadItemClick(int positon);
    }
}
