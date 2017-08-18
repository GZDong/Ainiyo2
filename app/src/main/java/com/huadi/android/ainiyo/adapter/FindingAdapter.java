package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by fengsam on 17-7-26.
 */

public class FindingAdapter extends BaseAdapter {

    private List<FindingInfo> mList;
    private ImageAdapter mAdapter;
    private Context mContext;

    public FindingAdapter(Context mContext, List<FindingInfo> list) {
        this.mContext = mContext;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public FindingInfo getItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.finding_list_row, null);
            holder = new ViewHolder();
            ViewUtils.inject(holder, convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FindingInfo findingInfo = mList.get(position);
        if (findingInfo.getAge() != 0) {
            holder.iv_finding_age.setText(findingInfo.getAge());
        }
//        if (findingInfo.getMatchpercent()!=null)
//        {
//            holder.tv_finding_match_percent.setText(findingInfo.getMatchpercent());
//        }
        if (findingInfo.getId() != null) {
            holder.iv_finding_name.setText(findingInfo.getId());
        }
        if (findingInfo.getJob() != null) {
            holder.tv_finding_job.setText(findingInfo.getJob());
        }
//        if(findingInfo.getAvatar()!=null)
//        {
//            Glide.with(parent.getContext()).load(findingInfo.getAvatar()).into(holder.iv_finding_pic);
//        }

        return convertView;
    }

    class ViewHolder {

        @ViewInject(R.id.iv_finding_pic)
        ImageView pic_head;
        @ViewInject(R.id.tv_finding_name)
        TextView iv_finding_name;
        @ViewInject(R.id.tv_finding_age)
        TextView iv_finding_age;
        @ViewInject(R.id.iv_finding_pic)
        ImageView iv_finding_pic;
        @ViewInject(R.id.tv_finding_match_percent)
        TextView tv_finding_match_percent;
        @ViewInject(R.id.tv_finding_job)
        TextView tv_finding_job;

    }
}
