package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hp.hpl.sparta.Text;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.MovementDetailActivity;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.entity.MovementContentData;
import com.huadi.android.ainiyo.entity.MovementData;
import com.huadi.android.ainiyo.entity.MovementResult;
import com.huadi.android.ainiyo.entity.ResponseObject;
import com.huadi.android.ainiyo.frag.MovementFragment;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.huadi.android.ainiyo.util.CONST.ATTEND_ACTIVITY;
import static com.huadi.android.ainiyo.util.CONST.RETURN_MODE;

/**
 * Created by fengsam on 17-7-26.
 */

public class MovementAdapter extends BaseAdapter {

    private List<MovementContentData> mList;
    private String sessionId;
    private ImageAdapter mAdapter;
    private Context father;
    private boolean isJoinedMode = false;


    public MovementAdapter(List<MovementContentData> list, String session,boolean joined) {
        mList = list;
        sessionId = session;
        isJoinedMode = joined;
    }

    public void setFather(Context father) {
        this.father = father;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public MovementContentData getItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position);
    }

    public String getPhoItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position).getImageUrl();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movement_list_row, null);
            holder = new ViewHolder();
            ViewUtils.inject(holder, convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MovementContentData mcd = mList.get(position);

        if (mcd.getTitle() != null) {
            holder.title.setText(mcd.getTitle());
        }

        if (mcd.getDate() != null) {
            holder.date.setText(mcd.getDate());
        }

        if (mcd.getArticle() != null) {
            if (mcd.getArticle().length() >= 59) {
                String previewText = mcd.getArticle().substring(0, 58) + " ……";
                holder.article.setText(previewText);//预览显示少部分文字
            }
            else {
                holder.article.setText(mcd.getArticle());
            }
        }

        if (mcd.getImageUrl() != null) {

            final String image = mcd.getImageUrl();
            Glide.with(parent.getContext()).load(image).into(holder.pic);

        }

        Log.e("MOVEADA",String.valueOf(mcd.getId())+mcd.getTitle()+String.valueOf(mcd.isJoined()));
        if(mcd.isJoined()){
            holder.join.setText("已参加");
            holder.join.setEnabled(false);
            holder.join.setBackgroundColor(Color.GRAY);
        }
        else {
            holder.join.setText("我要参加");
            holder.join.setEnabled(true);
            holder.join.setBackgroundColor(Color.parseColor("#478bf8"));
        }

        if(isJoinedMode){
            holder.join.setVisibility(View.GONE);
        }
        else {
            {
                holder.join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final TextView thisView = (TextView)view;
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("sessionid", sessionId);
                        params.addBodyParameter("aid", String.valueOf(mcd.getId()));


                        new HttpUtils().send(HttpRequest.HttpMethod.POST, ATTEND_ACTIVITY, params, new RequestCallBack<String>() {

                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.i("MOVEMENT_ADAPTER", "JOINED");
                                mList.get(position).setJoined(true);
                                thisView.setText("已参加");
                                thisView.setEnabled(false);
                                thisView.setBackgroundColor(Color.GRAY);
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                //Toast.makeText(MovementAdapter.this.father,"网络异常",Toast.LENGTH_SHORT).show();
                            }
                        });

//                        if(mList.get(position).isJoined()){
//                            thisView.setText("已参加");
//                            thisView.setEnabled(false);
//                        }


                    }
                });
            }


        }

        return convertView;
    }



    class ViewHolder {
        @ViewInject(R.id.iv_movement_pic)
        ImageView pic;

        @ViewInject(R.id.tv_movement_title)
        TextView title;

        @ViewInject(R.id.tv_movement_date)
        TextView date;

        @ViewInject(R.id.tv_movement_content)
        TextView article;

        @ViewInject(R.id.btn_movement_tpi)
        TextView join;
    }
}
