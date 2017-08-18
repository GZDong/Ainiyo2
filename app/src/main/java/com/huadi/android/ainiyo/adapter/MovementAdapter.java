package com.huadi.android.ainiyo.adapter;

import android.content.Context;
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


    public MovementAdapter(List<MovementContentData> list,String session) {
        mList = list;
        sessionId = session;
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

        final MovementContentData mcd = mList.get(position);

        if(mcd.getTitle() != null){
            holder.title.setText(mcd.getTitle());
        }

        if(mcd.getDate() != null){
            holder.date.setText(mcd.getDate());
        }

        if(mcd.getArticle() != null){
            holder.article.setText(mcd.getArticle());
        }

        if (mcd.getImageUrl() != null) {
//            if(modeInfo.getImgUrlforContent().size()==1) {
            final String image = mcd.getImageUrl();
            Glide.with(parent.getContext()).load(image).into(holder.pic);

            // Glide.with(parent.getContext()).load("http://120.24.168.102:8080/getalumb?sessionid=5ca6b5f4b438030f123fb149ff19fd8769365789").skipMemoryCache(false).into(holder.pic_content);
//            }else{
//                mAdapter.refresh(modeInfo.getImgUrlforContent());
//            }
        }


        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ECApplication application = (ECApplication) getActivity().getApplication();
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("aid",String.valueOf(mcd.getId()));
                //params.addBodyParameter("type", "1");

                new HttpUtils().send(HttpRequest.HttpMethod.POST, ATTEND_ACTIVITY, params, new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Toast.makeText(father,"参加成功",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
            }
        });



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
        Button join;
    }
}
