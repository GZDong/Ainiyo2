package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.ModeComment;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengsam on 17-8-13.
 */

public class ModeToCommentAdapter extends BaseAdapter {

    ViewHolder holder = null;
    private LayoutInflater mLayoutInflater;
    private ArrayList<ModeComment> mList;
    private Context mContext;

    public ModeToCommentAdapter(Context context, ArrayList<ModeComment> mCommentList) {
        this.mContext = context;
        this.mList = mCommentList;
    }


    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ModeComment getItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        if (converView == null) {
            converView = mLayoutInflater.from(parent.getContext()).inflate(R.layout.mode_to_comment_list_row, null);
            holder = new ViewHolder();
            ViewUtils.inject(holder, converView);

            converView.setTag(holder);
        } else {
            holder = (ViewHolder) converView.getTag();
        }
        ModeComment mc = mList.get(position);

        if (mc.getContent() != null) {
            holder.tv_mode_to_comment_content.setText(mc.getContent());
        }
        if (mc.getReplyed() != null) {
            holder.tv_mode_to_comment_replyed.setText(mc.getReplyed());
        }
        if (mc.getTime() != null) {
            holder.tv_mode_to_comment_time.setText(mc.getTime().substring(0, 10));
        }

        if (UserInfoLab.get(mContext).getUserInfo().getId() != null && mc.getUserid() != null) {
            if (mc.getUserid().equals(UserInfoLab.get(mContext).getUserInfo().getId())) {
                //Toast.makeText(mContext,"mymood",Toast.LENGTH_SHORT).show();
                //Log.i("imagehead", UserInfoLab.get(mContext).getUserInfo().getPicUrl());
                holder.tv_mode_to_comment_reply.setText(UserInfoLab.get(mContext).getUserInfo().getUsername());
                Glide.with(mContext).load(UserInfoLab.get(mContext).getUserInfo().getPicUrl()).placeholder(R.mipmap.ic_default_avater).into(holder.mode_to_comment_pic_head);
            } else {
                //Toast.makeText(mContext,"myid: "+String.valueOf(mc.getId())+"  myLabid: "+String.valueOf(UserInfoLab.get(mContext).getUserInfo().getId()),Toast.LENGTH_SHORT).show();
                holder.tv_mode_to_comment_reply.setText(FriendsLab.get(mContext).findNameById(mc.getUserid()));
                Glide.with(mContext).load(FriendsLab.get(mContext).findUrlById(mc.getUserid())).placeholder(R.mipmap.ic_default_avater).into(holder.mode_to_comment_pic_head);
            }
        }


        return converView;
    }

    class ViewHolder {
        @ViewInject(R.id.mode_to_comment_pic_head)
        ImageView mode_to_comment_pic_head;
        @ViewInject(R.id.tv_mode_to_comment_reply)
        TextView tv_mode_to_comment_reply;
        @ViewInject(R.id.tv_mode_to_comment_replyed)
        TextView tv_mode_to_comment_replyed;
        @ViewInject(R.id.tv_mode_to_comment_content)
        TextView tv_mode_to_comment_content;
        @ViewInject(R.id.tv_mode_to_comment_time)
        TextView tv_mode_to_comment_time;
    }
}
