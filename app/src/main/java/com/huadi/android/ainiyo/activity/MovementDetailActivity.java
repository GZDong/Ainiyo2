package com.huadi.android.ainiyo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MovementDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.wv_movement_details)
    private WebView wv_movement_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_detail);
        ViewUtils.inject(this);

        WebSettings webSettings = wv_movement_details.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setJavaScriptEnabled(true);
        //String body ="示例：这里有个img标签，地址是相对路径<img src='/uploads/allimg/130923/1FP02V7-0.png' />";
        wv_movement_details.loadUrl("http://www.baidu.com");
        //覆盖第三方浏览器打开网页行为
        wv_movement_details.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true网页在webview打开,false为第三方浏览器
                view.loadUrl(url);
                return true;
            }


        });
        // wv_movement_details.loadData("http://www.baidu.com", "text/html", "utf-8");
    }


    @OnClick({R.id.movement_detail_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.movement_detail_back:
                ToolKits.putInt(MovementDetailActivity.this, "fragment", 3);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wv_movement_details.canGoBack()) {
                wv_movement_details.goBack();
                return true;
            } else {
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);

    }


}
