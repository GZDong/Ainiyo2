package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
    private OnPiPeiItemClickListener mPiPeiItemClickListener;
    private PopupWindow mPopWindow;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            holder.iv_finding_age.setText(String.valueOf(findingInfo.getAge()));
        }
//        if (findingInfo.getMatchpercent()!=null)
//        {
//            holder.tv_finding_match_percent.setText(findingInfo.getMatchpercent());
//        }
        if (findingInfo.getName() != null) {
            holder.iv_finding_name.setText(findingInfo.getName());
        }
        if (findingInfo.getJob() != null) {
            holder.tv_finding_job.setText(findingInfo.getJob());
        }
//        if(findingInfo.getAvatar()!=null)
//        {
//            Glide.with(parent.getContext()).load(findingInfo.getAvatar()).into(holder.iv_finding_pic);
//        }

        holder.ll_finding_pipei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPiPeiItemClickListener.OnPiPeiItemClick(position);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //设置contentView
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.mode_comment_delete_pop_window, null);
                mPopWindow = new PopupWindow(contentView,
                        WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                mPopWindow.setContentView(contentView);
                //设置各个控件的点击响应
                TextView tv1 = (TextView) contentView.findViewById(R.id.pop_computer);
                TextView tv2 = (TextView) contentView.findViewById(R.id.pop_financial);
                tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(ModeDetailNineGridActivity.this,"clicked Delete",Toast.LENGTH_SHORT).show();
                        DeleteComment(position);
                        mPopWindow.dismiss();
                        //Toast.makeText(ModeDetailNineGridActivity.this,mCommentList.get(position).getContent(),Toast.LENGTH_SHORT).show();
                    }
                });

                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(ModeDetailNineGridActivity.this,"clicked cancel",Toast.LENGTH_SHORT).show();
                        mPopWindow.dismiss();
                    }
                });
                //显示PopupWindow
                View rootview = LayoutInflater.from(mContext).inflate(R.layout.activity_finding_detail, null);
                mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                return false;
            }
        });

        return convertView;
    }

    private void DeleteComment(int position) {
        mList.remove(position);
        this.notifyDataSetChanged();
    }

    public void setOnPipeiItemClickListener(OnPiPeiItemClickListener listener) {
        this.mPiPeiItemClickListener = listener;
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
        @ViewInject(R.id.ll_finding_pipei)
        LinearLayout ll_finding_pipei;

    }

    public interface OnPiPeiItemClickListener {
        void OnPiPeiItemClick(int position);
    }
}
