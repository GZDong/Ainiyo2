package com.huadi.android.ainiyo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.ModeComment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengsam on 17-8-13.
 */

public class ModeCommentAdapter extends BaseAdapter {

    ViewHolder holder = null;
    private LayoutInflater mLayoutInflater;
    private List<ModeComment> mList;
    private Context mContext;
    private PopupWindow editWindow;
    private View rootView;
    private ModeToCommentAdapter mToCommentAdapter;
    private ArrayList<ModeComment> mcList;

    public ModeCommentAdapter(Context context, List<ModeComment> mCommentList) {
        this.mContext = context;
        this.mList = mCommentList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return (mList == null || position >= mList.size()) ? null : mList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        if (converView == null) {
            converView = mLayoutInflater.from(parent.getContext()).inflate(R.layout.mode_comment_list_row, null);
            holder = new ViewHolder();
            ViewUtils.inject(holder, converView);

            converView.setTag(holder);
        } else {
            holder = (ViewHolder) converView.getTag();
        }
        ModeComment mc = mList.get(position);
        if (mc.getName() != null) {
            holder.mode_comment_name.setText(mc.getName());
        }
        if (mc.getContent() != null) {
            holder.tv_mode_comment_content.setText(mc.getContent());
        }
        if (mc.getTime() != null) {
            holder.tv_mode_comment_time.setText(mc.getTime());
        }

        LoadToCommentData();

        return converView;
    }

    public void LoadToCommentData() {
        ModeComment mc1 = new ModeComment("", "fengsam", "", "hihi", "", "David");
        ModeComment mc2 = new ModeComment("", "David", "", ".....", "", "fengsam");
        mcList = new ArrayList<ModeComment>();
        mcList.add(mc1);
        mcList.add(mc2);
        mToCommentAdapter = new ModeToCommentAdapter(mContext, mcList);
        holder.lv_to_comments.setAdapter(mToCommentAdapter);
    }

    class ViewHolder {
        @ViewInject(R.id.mode_comment_pic_head)
        ImageView mode_comment_pic_head;
        @ViewInject(R.id.mode_comment_name)
        TextView mode_comment_name;
        @ViewInject(R.id.tv_mode_comment_content)
        TextView tv_mode_comment_content;
        @ViewInject(R.id.tv_mode_comment_time)
        TextView tv_mode_comment_time;
        @ViewInject(R.id.lv_to_comments)
        ListView lv_to_comments;


        @OnItemClick({R.id.lv_to_comments})
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            comment();
            Toast.makeText(mContext, "To" + String.valueOf(position), Toast.LENGTH_SHORT).show();
        }
    }


    private void comment() {

        View editView = mLayoutInflater.inflate(R.layout.mode_comment_reply_input, null);
        rootView = mLayoutInflater.inflate(R.layout.mode_comment_list_row, null);
        editWindow = new PopupWindow(editView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editWindow.setOutsideTouchable(true);
        editWindow.setFocusable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        final EditText replyEdit = (EditText) editView.findViewById(R.id.mode_et_msg);
        replyEdit.setFocusable(true);
        replyEdit.requestFocus();
        // 以下两句不能颠倒
        editWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        editWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        editWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        // 显示键盘
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        editWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
            }
        });

        Button btn = (Button) editView.findViewById(R.id.mode_b_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(replyEdit.getText())) {
                    String info = replyEdit.getText().toString();
                    updateComment(info);
                } else {
                    Toast.makeText(mContext, "请输入内容后在留言", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //这边应该上传用户留言，然后刷新界面
    private void updateComment(String inf) {
        ModeComment comment = null;
        ModeComment mc3 = new ModeComment("", "David", "", inf, "", "fengsam");
        //mcList.clear();
        mcList.add(0, mc3);

        mToCommentAdapter.notifyDataSetChanged();
        mToCommentAdapter.notifyDataSetInvalidated();

        Toast.makeText(mContext, mToCommentAdapter.getItem(0).getContent(), Toast.LENGTH_SHORT).show();
    }
}
