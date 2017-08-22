package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.FindingDataAnlaysisActivity;
import com.huadi.android.ainiyo.activity.FindingUserInfoActivity;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengsam on 17-8-19.
 */

public class FindingCardSwlpeAdapter extends RecyclerView.Adapter {

    private List<FindingInfo> mList;
    //private List<Integer> mList = new ArrayList<>();
    private Context mContext;
    private MyViewHolder myHolder;

    public MyViewHolder getViewHolder() {
        return myHolder;
    }

    public FindingCardSwlpeAdapter(Context mContext, List<FindingInfo> list) {
        this.mContext = mContext;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finding_list_row, parent, false);
        myHolder = new MyViewHolder(view);


        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //ImageView avatarImageView = ((MyViewHolder) holder).avatarImageView;
        //myHolder.avatarImageView.setImageResource(mList.get(position));

        DecimalFormat df = new DecimalFormat("#.##");
        final FindingInfo findingInfo = mList.get(position);
        if (findingInfo.getAge() != 0) {
            myHolder.iv_finding_age.setText(String.valueOf(findingInfo.getAge()));
        }
//        if (findingInfo.getMatchpercent()!=null)
//        {
//            holder.tv_finding_match_percent.setText(findingInfo.getMatchpercent());
//        }
        if (findingInfo.getName() != null) {
            myHolder.iv_finding_name.setText(findingInfo.getName());
        }
        if (findingInfo.getJob() != null) {
            myHolder.tv_finding_job.setText(findingInfo.getJob());
        }
        if (findingInfo.getSummary() != 0) {
            myHolder.tv_finding_match_percent.setText(String.valueOf(df.format(findingInfo.getSummary())));
        }
//        if(findingInfo.getAvatar()!=null)
//        {
//            Glide.with(parent.getContext()).load(findingInfo.getAvatar()).into(holder.iv_finding_pic);
//        }
        myHolder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(mContext, FindingUserInfoActivity.class);
                t.putExtra("findinginfo", findingInfo);
                mContext.startActivity(t);
                //Toast.makeText(mContext, "position: " + String.valueOf(0) + "view: " + mList.get(0).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        myHolder.ll_finding_pipei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FindingDataAnlaysisActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("findingdataitem", mList.get(0));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.iv_finding_pic)
        ImageView avatarImageView;
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
        @ViewInject(R.id.ll_finding_pipei)
        LinearLayout ll_finding_pipei;
        @ViewInject(R.id.ll_finding_all)
        LinearLayout ll_finding_all;
        @ViewInject(R.id.iv_finding_like)
        public ImageView likeImageView;
        @ViewInject(R.id.iv_finding_dislike)
        public ImageView dislikeImageView;

        View myView;
        MyViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            ViewUtils.inject(this, itemView);

        }

    }
}
