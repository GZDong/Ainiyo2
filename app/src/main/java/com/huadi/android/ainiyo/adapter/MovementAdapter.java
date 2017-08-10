package com.huadi.android.ainiyo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by fengsam on 17-7-26.
 */

public class MovementAdapter extends BaseAdapter {

    private List<ModeInfo> mList;
    private ImageAdapter mAdapter;

    public MovementAdapter(List<ModeInfo> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ModeInfo getItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position);
    }

    public String getPhoItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position).getImgUrlforContent().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movement_list_row, null);
            holder = new ViewHolder();
            ViewUtils.inject(holder, convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ModeInfo modeInfo = mList.get(position);
        if (modeInfo.getName() != null) {
            holder.mode_username.setText(modeInfo.getName());
        }
        if (modeInfo.getContent() != null) {
            holder.mode_content.setText(modeInfo.getContent());
        }
        if (modeInfo.getImgUrlforHead() != null) {

        }
        if (modeInfo.getImgUrlforContent() != null) {
//            if(modeInfo.getImgUrlforContent().size()==1) {
            final String image = modeInfo.getImgUrlforContent().get(0);
            Glide.with(parent.getContext()).load(image).into(holder.pic_content);

            // Glide.with(parent.getContext()).load("http://120.24.168.102:8080/getalumb?sessionid=5ca6b5f4b438030f123fb149ff19fd8769365789").skipMemoryCache(false).into(holder.pic_content);
//            }else{
//                mAdapter.refresh(modeInfo.getImgUrlforContent());
//            }
        }
        return convertView;
    }

    class ViewHolder {

        @ViewInject(R.id.mode_pic_head)
        ImageView pic_head;
        @ViewInject(R.id.mode_pic_content)
        ImageView pic_content;
        @ViewInject(R.id.mode_username)
        TextView mode_username;
        @ViewInject(R.id.mode_content)
        TextView mode_content;
    }
}
