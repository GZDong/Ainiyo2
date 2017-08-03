package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


import java.io.File;
import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {


    @ViewInject(R.id.iv_mode_show_pho)
    ImageView iv_mode_show_pho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ViewUtils.inject(this);
        final Intent intent=getIntent();
        String mPho=intent.getStringExtra("image");
        Glide.with(this).load(new File(mPho)).into(iv_mode_show_pho);
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
