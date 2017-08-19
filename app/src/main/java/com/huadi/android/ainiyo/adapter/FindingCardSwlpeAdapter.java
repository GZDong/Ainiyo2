package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.FindingInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengsam on 17-8-19.
 */

public class FindingCardSwlpeAdapter extends RecyclerView.Adapter {

    // private List<FindingInfo> mList;
    private List<Integer> mList = new ArrayList<>();
    private Context mContext;

    public FindingCardSwlpeAdapter(Context mContext, List<Integer> list) {
        this.mContext = mContext;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finding_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView avatarImageView = ((MyViewHolder) holder).avatarImageView;
        avatarImageView.setImageResource(mList.get(position));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView username;

        MyViewHolder(View itemView) {
            super(itemView);
            avatarImageView = (ImageView) itemView.findViewById(R.id.iv_finding_pic);
            username = (TextView) itemView.findViewById(R.id.tv_finding_name);
        }

    }
}
