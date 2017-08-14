package com.huadi.android.ainiyo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.ModeCommentAdapter;
import com.huadi.android.ainiyo.entity.ModeComment;
import com.huadi.android.ainiyo.entity.ModeInfo;
import com.huadi.android.ainiyo.entity.ModeLocalData;
import com.huadi.android.ainiyo.entity.ModeWebData;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;

public class ModeDetailNineGridActivity extends AppCompatActivity {

    @ViewInject(R.id.nineGrid)
    NineGridView nineGridView;
    @ViewInject(R.id.tv_mode_nine_grid_content)
    TextView tv_content;
    @ViewInject(R.id.tv_createTime)
    TextView tv_createTime;
    @ViewInject(R.id.lv_comments)
    ListView comments;
    @ViewInject(R.id.mode_ll_liuyan)
    LinearLayout mAmLlLiuyan;
    @ViewInject(R.id.mode_b_save)
    Button mAmBSave;
    @ViewInject(R.id.mode_et_msg)
    EditText mAmEtMsg;
    @ViewInject(R.id.sv_comment)
    ScrollView sv_comment;

    private PopupWindow editWindow;
    private View rootView;
    private NineGridViewClickAdapter mGridAdapter;
    private ModeCommentAdapter mCommentAdapter;

    //列表数据
    ArrayList<ModeComment> mCommentList;
    //回复的内容
    String info = "";
    //标记位，是评论还是回复。默true认评论
    boolean isComment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_detail_nine_grid);

        ViewUtils.inject(this);


        NineGridView.setImageLoader(new GlideImageLoader());
        final Intent intent = getIntent();
        ModeLocalData mld = (ModeLocalData) intent.getSerializableExtra("item");
        //加载九宫格图片
        LoadNineGridPho(mld.getMi().getImgUrlforContent());
        tv_content.setText(mld.getMi().getContent());
        tv_createTime.setText(mld.getDate());

        initCommentData();

        initCommentAdapter();
    }

    //加载九宫格图片
    public void LoadNineGridPho(ArrayList<String> mPhoList) {
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if (mPhoList != null) {
            for (String pl : mPhoList) {
                ImageInfo info = new ImageInfo();
                info.setBigImageUrl(pl);
                info.setThumbnailUrl(pl);
                imageInfo.add(info);
                //Toast.makeText(ModeDetailNineGridActivity.this,pl,Toast.LENGTH_SHORT).show();
            }
        }
        mGridAdapter = new NineGridViewClickAdapter(ModeDetailNineGridActivity.this, imageInfo);
        nineGridView.setAdapter(mGridAdapter);
    }

    private void initCommentData() {
        mCommentList = new ArrayList<>();
        ModeComment mc = null;
        mc = new ModeComment("1", "David", "", "hihi", "8-13", "");
        mCommentList.add(mc);
        mCommentList.add(mc);

    }


    private void initCommentAdapter() {
        mCommentAdapter = new ModeCommentAdapter(this, mCommentList);
        comments.setAdapter(mCommentAdapter);
    }

    /**
     * Glide 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    @OnItemClick({R.id.lv_comments})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(ModeDetailNineGridActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.btn_mode_nine_grid_comment, R.id.mode_b_save, R.id.mode_detail_nine_grid_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mode_nine_grid_comment:
                comment(1);
                break;
//            case R.id.mode_b_save:
//                saveComment();
//                break;
            case R.id.mode_detail_nine_grid_back:
                ToolKits.putInt(this, "fragment", 2);
                finish();
                break;
        }
    }


    //这边应该上传用户留言，然后刷新界面
    private void updateComment(String inf, int i) {
        ModeComment comment = null;
        if (i == 1) {
            comment = new ModeComment(666 + "", "张三" + 666, "http://d.hiphotos.baidu.com/image/h%3D360/sign=856d60650933874483c5297a610fd937/55e736d12f2eb938e81944c7d0628535e5dd6f8a.jpg", inf, "2015-03-04 23:02:06", null);
            mCommentList.add(0, comment);
            mCommentAdapter.notifyDataSetChanged();
        }
        if (i == 2) {

        }
    }


    private void comment(final int i) {

        View editView = LayoutInflater.from(ModeDetailNineGridActivity.this).inflate(R.layout.mode_comment_reply_input, null);
        rootView = LayoutInflater.from(ModeDetailNineGridActivity.this).inflate(R.layout.activity_mode_detail_nine_grid, null);
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
        final InputMethodManager imm = (InputMethodManager) ModeDetailNineGridActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    info = replyEdit.getText().toString();
                    updateComment(info, i);
                } else {
                    Toast.makeText(ModeDetailNineGridActivity.this, "请输入内容后在留言", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
