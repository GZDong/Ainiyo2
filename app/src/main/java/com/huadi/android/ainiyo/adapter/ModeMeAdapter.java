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
import com.huadi.android.ainiyo.activity.ModeMeActivity;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.entity.ModeLocalData;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengsam on 17-7-26.
 */

public class ModeMeAdapter extends BaseAdapter {

    private List<ModeLocalData> mList;
    private ImageAdapter mAdapter;
    private OnDeleteItemClickListener mDeleteItemClickListener;
    private Context mContext;

    public ModeMeAdapter(Context context, List<ModeLocalData> list) {
        mContext = context;
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.mode_me_lsit_row, null);
            holder=new ViewHolder();
            ViewUtils.inject(holder, convertView);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        ModeInfo modeInfo = mList.get(position).getMi();

        //绑定内容
        if(modeInfo.getContent()!=null) {
            holder.mode_content.setText(modeInfo.getContent());
        }
        //绑定日期
        String date = mList.get(position).getDate();
        if (date != null) {
            holder.mode_me_date.setText(date.substring(0, 10));
        }

        String mode_me_user_id = String.valueOf(mList.get(position).getUserid());
        if (UserInfoLab.get(mContext).getUserInfo().getId() != null && mode_me_user_id != null) {
            if (mode_me_user_id.equals(UserInfoLab.get(mContext).getUserInfo().getId())) {
                // Toast.makeText(mContext,"mymood",Toast.LENGTH_SHORT).show();
                // Log.i("imagehead", UserInfoLab.get(mContext).getUserInfo().getPicUrl());
                holder.mode_username.setText(UserInfoLab.get(mContext).getUserInfo().getUsername());
                Glide.with(mContext).load(UserInfoLab.get(mContext).getUserInfo().getPicUrl()).placeholder(R.mipmap.ic_default_avater).into(holder.pic_head);
            } else {
                // Toast.makeText(mContext,"myid: "+mode_me_user_id+"  myLabid: "+String.valueOf(UserInfoLab.get(mContext).getUserInfo().getId()),Toast.LENGTH_SHORT).show();
                holder.mode_username.setText(FriendsLab.get(mContext).findNameById(mode_me_user_id));
                Glide.with(mContext).load(FriendsLab.get(mContext).findUrlById(mode_me_user_id)).placeholder(R.mipmap.ic_default_avater).into(holder.pic_head);
            }
        }


        if (modeInfo.getImgUrlforContent()!=null)
        {
//            if(modeInfo.getImgUrlforContent().size()==1) {
            final String image = modeInfo.getImgUrlforContent().get(0);
            Glide.with(parent.getContext()).load(image).into(holder.pic_content);
            // Glide.with(parent.getContext()).load(new File(image)).into(holder.pic_content);

            // Glide.with(parent.getContext()).load("http://120.24.168.102:8080/getalumb?sessionid=5ca6b5f4b438030f123fb149ff19fd8769365789").skipMemoryCache(false).into(holder.pic_content);
//            }else{
//                mAdapter.refresh(modeInfo.getImgUrlforContent());
//            }
        }

        holder.mode_me_delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                mDeleteItemClickListener.OnDeleteItemClick(position);
            }
        });
        return convertView;
    }

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener listener) {
        this.mDeleteItemClickListener = listener;
    }

    class ViewHolder{

        @ViewInject(R.id.mode_me_pic_head)
        ImageView pic_head;
        @ViewInject(R.id.mode_me_pic_content)
        ImageView pic_content;
        @ViewInject(R.id.mode_me_username)
        TextView mode_username;
        @ViewInject(R.id.mode_me_content)
        TextView mode_content;
        @ViewInject(R.id.mode_me_delete)
        ImageView mode_me_delete;
        @ViewInject(R.id.mode_me_date)
        TextView mode_me_date;
    }

//    @OnClick(R.id.mode_me_delete)
//    public void onClick(View v)
//    {
//        switch (v.getId())
//        {
//            case R.id.mode_me_delete :
//                //ToolKits.DeletingModeData(mContext,"modeMeInfoList",position-1);
//                mDeleteItemClickListener.OnDeleteItemClick(position);
//                Log.i("test1","delete success");
//                break;
//        }
//    }

    public interface OnDeleteItemClickListener{
        void OnDeleteItemClick(int position);
    }
}
