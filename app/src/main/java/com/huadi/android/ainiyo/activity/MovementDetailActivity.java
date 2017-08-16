package com.huadi.android.ainiyo.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.Retrofit2.GetRequest_party;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovementDetailActivity extends AppCompatActivity {


    @ViewInject(R.id.movement_detail_scroll_content)
    private ScrollView sv;

    @ViewInject(R.id.partyTitleView)
    TextView title;
    @ViewInject(R.id.partyDateView)
    TextView date;
    @ViewInject(R.id.partyImage)
    ImageView image;
    @ViewInject(R.id.partyMainText)
    TextView article;
    private ProgressDialog dialog;

    public class PartyInformation {
        String title;
        String date;
        String picture;
        String article;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_detail);
        ViewUtils.inject(this);
        LoadAndShow(getIntent().getStringExtra("id"));

//        WebSettings webSettings = wv_movement_details.getSettings();
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setJavaScriptEnabled(true);
//        //String body ="示例：这里有个img标签，地址是相对路径<img src='/uploads/allimg/130923/1FP02V7-0.png' />";
//        wv_movement_details.loadUrl("http://www.baidu.com");
//        //覆盖第三方浏览器打开网页行为
//        wv_movement_details.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //返回值是true网页在webview打开,false为第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
//
//
//        });
//
//        //WebView加载页面优先使用缓存加载
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

//        //进程条
//        wv_movement_details.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                //newProgress 1-100之间的整数
//                if (newProgress==100)
//                {
//                    //网页加载完毕,关闭ProgressDialog
//                    closeDialog();
//                }
//                else {
//                    //网页正在加载,打开ProgressDialog
//                    openDialog(newProgress);
//                }
//            }
//        });


        // wv_movement_details.loadData("http://www.baidu.com", "text/html", "utf-8");


    }

//    private void openDialog(int newProgress) {
//        if(dialog==null){
//            dialog=new ProgressDialog(MovementDetailActivity.this);
//            dialog.setTitle("正在加载中");
//            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            dialog.setProgress(newProgress);
//            dialog.show();
//        }
//        else {
//            dialog.setProgress(newProgress);
//        }
//    }
//
//    private void closeDialog() {
//        if(dialog!=null&&dialog.isShowing())
//        {
//            dialog.dismiss();
//            dialog=null;
//        }
//    }

    private void LoadAndShow(final String id) {
        Retrofit caller = new Retrofit.Builder().baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ECApplication ecApplication =(ECApplication) getApplication();

        GetRequest_party request = caller.create(GetRequest_party.class);

        request.getPartyInformation(ecApplication.sessionId,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PartyInformation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PartyInformation partyInformation) {
                        title.setText(partyInformation.title);
                        date.setText(partyInformation.date);
                        article.setText(partyInformation.article);

                        URL url = null;
                        Bitmap bitmap = null;

                        try{
                            url = new URL(partyInformation.picture);
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            connection.setConnectTimeout(6000);
                            connection.setDoInput(true);
                            InputStream is = connection.getInputStream();

                            bitmap = BitmapFactory.decodeStream(is);
                            image.setImageBitmap(bitmap);

                            is.close();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }



                    }
                });




    }






    @OnClick({R.id.movement_detail_back,R.id.btn_join_now})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.movement_detail_back:
                ToolKits.putInt(MovementDetailActivity.this, "fragment", 3);
                finish();
                break;
            case R.id.btn_join_now:
                //
                break;
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (wv_movement_details.canGoBack()) {
//                wv_movement_details.goBack();
//                return true;
//            } else {
                MovementDetailActivity.this.finish();
//            }
        }

        return super.onKeyDown(keyCode, event);

    }


}
