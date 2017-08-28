package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.ActivityDetil;
import com.huadi.android.ainiyo.activity.BigImageActivity;
import com.huadi.android.ainiyo.entity.ActivityEntity;

import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by xiaoxing on 2017/8/28.
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder>{


    private Context mContext;
    private List<ActivityEntity> mActivityEntity;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View activityView;
        TextView title;
        TextView date;
        TextView article;
        ImageView image;
        public ViewHolder(View view){
            super(view);
            activityView=view;
            title=(TextView)view.findViewById(R.id.title_add);
            date=(TextView)view.findViewById(R.id.date_add);
            article=(TextView)view.findViewById(R.id.article_add);
            image=(ImageView) view.findViewById(R.id.image_add);
        }
    }
    public ActivityAdapter(Context context,List<ActivityEntity> ActivityEntity){
        mContext=context;
        mActivityEntity=ActivityEntity;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_join,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder,int position){
        holder.title.setText(mActivityEntity.get(position).getTitle());
        holder.date.setText(mActivityEntity.get(position).getDate());
        holder.article.setText(mActivityEntity.get(position).getArticle());
        Glide.with(mContext).load(mActivityEntity.get(position).getImageUrl()).into(holder.image);


        //点击事件
        holder.activityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String TITLE = mActivityEntity.get(position).getTitle();
                String DATE=mActivityEntity.get(position).getDate();
                String ARTICLE=mActivityEntity.get(position).getArticle();
                Intent intent = new Intent(mContext, ActivityDetil.class);
                intent.putExtra("title", TITLE);
                intent.putExtra("date", DATE);
                intent.putExtra("article", ARTICLE);
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount(){
        return mActivityEntity.size();
    }
}
