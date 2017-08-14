package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.ModeComment;
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
        if (mc.getName() != null) {
            holder.tv_mode_to_comment_reply.setText(mc.getName());
        }
        if (mc.getContent() != null) {
            holder.tv_mode_to_comment_content.setText(mc.getContent());
        }
        if (mc.getTime() != null) {
            holder.tv_mode_to_comment_replyed.setText(mc.getReplyed());
        }


        return converView;
    }

    class ViewHolder {
        @ViewInject(R.id.tv_mode_to_comment_reply)
        TextView tv_mode_to_comment_reply;
        @ViewInject(R.id.tv_mode_to_comment_replyed)
        TextView tv_mode_to_comment_replyed;
        @ViewInject(R.id.tv_mode_to_comment_content)
        TextView tv_mode_to_comment_content;
    }
}
