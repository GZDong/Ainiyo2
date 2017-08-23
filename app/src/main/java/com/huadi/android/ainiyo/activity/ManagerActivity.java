package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnCheckedChange;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ManagerActivity extends AppCompatActivity {
    @ViewInject(R.id.manager_web)
    private WebView manager_web;
    @ViewInject(R.id.back)
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ViewUtils.inject(this);
        manager_web.getSettings().setJavaScriptEnabled(true);
        manager_web.setWebViewClient(new WebViewClient());
        manager_web.loadUrl("http://www.baidu.com");
    }
    @OnClick({R.id.manager_web,R.id.back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(ManagerActivity.this, MainActivity.class));
                break;

        }
    }
}
