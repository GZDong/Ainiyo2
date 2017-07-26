package com.huadi.android.ainiyo.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengsam on 17-7-26.
 */

public class ModeAdapter extends BaseAdapter {

    private List<ModeInfo> mList;

    public ModeAdapter(List<ModeInfo> list){ mList = list; }

    @Override
    public int getCount() {
        return mList == null ? 0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return (mList == null || position>=mList.size())?null:mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.mode_list_row, null);
            holder=new ViewHolder();
            ViewUtils.inject(holder, convertView);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        ModeInfo modeInfo=mList.get(position);
        holder.mode_username.setText(modeInfo.getName());
        holder.mode_content.setText(modeInfo.getContent());
        return convertView;
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
}
