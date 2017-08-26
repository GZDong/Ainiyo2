package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


import java.io.File;

public class ModePreviewActivity extends AppCompatActivity {


    @ViewInject(R.id.iv_mode_show_pho)
    ImageView iv_mode_show_pho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_preview);
        ViewUtils.inject(this);

        setImmersive();

        final Intent intent=getIntent();
        String mPho=intent.getStringExtra("image");
        Glide.with(this).load(mPho).into(iv_mode_show_pho);
    }

    public void setImmersive() {
        //设置状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.status_bar_mode_adding);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = ToolKits.getStatusBarHeight(this);
            //动态的设置隐藏布局的高度
            linear_bar.getLayoutParams().height = statusHeight;
        }
    }

    @OnClick({R.id.iv_mode_show_pho})
    public void OnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_mode_show_pho:
                finish();
                break;
        }
    }
}
