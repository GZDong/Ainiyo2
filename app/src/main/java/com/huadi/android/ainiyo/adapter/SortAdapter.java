package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.entry.Image;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.FriendsInfoActivity;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.util.ImgScaleUtil;


import java.util.List;

/**
 * @author: xp
 * @date: 2017/7/19
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Friends> mData;
    private Context mContext;   //*************
    private UserInfo mUserInfo;
    private static String TAG = "FriendsListActivity";

    public SortAdapter(Context context, List<Friends> data, UserInfo userInfo) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        mUserInfo = new UserInfo(userInfo.getUsername(), userInfo.getPassword());
        this.mContext = context;
    }

    @Override
    public SortAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTag = (TextView) view.findViewById(R.id.tag);
        viewHolder.tvName = (TextView) view.findViewById(R.id.name);
        viewHolder.imgView = (ImageView) view.findViewById(R.id.image_person);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SortAdapter.ViewHolder holder, final int position) {
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getLetters());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }

        holder.tvName.setText(this.mData.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, mData.get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, FriendsInfoActivity.class);
                intent.putExtra("name",mData.get(position).getName());
                intent.putExtra("picture",mData.get(position).getPicture());
                intent.putExtra("userInfo", mUserInfo);
                intent.putExtra("from", TAG);
                mContext.startActivity(intent);
            }
        });
        if (!TextUtils.isEmpty(mData.get(position).getPicUrl())) {
            Glide.with(mContext).load(mData.get(position).getPicUrl()).centerCrop().into(holder.imgView);
        } else {
            holder.imgView.setImageBitmap(ImgScaleUtil.decodeBitmapFromResource(mContext.getResources(), this.mData.get(position).getPicture(), 50, 50));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName;
        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    public void updateList(List<Friends> list){
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
