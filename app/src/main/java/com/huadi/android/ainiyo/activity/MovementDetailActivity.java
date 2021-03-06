package com.huadi.android.ainiyo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


import static com.huadi.android.ainiyo.util.CONST.ATTEND_ACTIVITY;
import static com.huadi.android.ainiyo.util.CONST.CANCEL_ACTIVITY;

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
    @ViewInject(R.id.btn_join_now)
    Button joinButton;
    @ViewInject(R.id.btn_cancel)
    Button cancelButton;

    private ProgressDialog dialog;


    private Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_detail);
        ViewUtils.inject(this);
        extras = getIntent().getExtras();
        LoadAndShow(extras);


    }

    private void LoadAndShow(final Bundle extras) {


        Glide.with(this).load(extras.getString("imageUrl")).into(image);

        title.setText(extras.getString("title"));
        date.setText(extras.getString("date"));
        article.setText(extras.getString("article"));


        if(extras.getBoolean("isJoined",false)){
            AfterJoin();
        } else {
            AfterCancel();
        }

    }

    private void SetUnJoinable(){
        joinButton.setBackground(getResources().getDrawable(R.drawable.movement_joined_selected_button));
        joinButton.setText(getResources().getString(R.string.has_joined));
        joinButton.setEnabled(false);
    }

    private void AfterJoin() {
        SetUnJoinable();
        cancelButton.setVisibility(View.VISIBLE);
        cancelButton.setEnabled(true);
    }

    private void AfterCancel() {
        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.setEnabled(false);

        joinButton.setBackground(getResources().getDrawable(R.drawable.movement_joined_button));
        joinButton.setText("我要参加");
        joinButton.setEnabled(true);
    }


    @OnClick({R.id.movement_detail_back, R.id.btn_join_now, R.id.partyImage, R.id.btn_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.movement_detail_back:
                ToolKits.putInt(MovementDetailActivity.this, "fragment", 3);
                finish();
                break;
            case R.id.btn_join_now:

                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", ((ECApplication) getApplication()).sessionId);
                params.addBodyParameter("aid", String.valueOf(extras.getInt("id")));


                new HttpUtils().send(HttpRequest.HttpMethod.POST, ATTEND_ACTIVITY, params, new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        AfterJoin();
                        //Toast.makeText(MovementDetailActivity.this, "参加成功", Toast.LENGTH_SHORT).show();
                        //?
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
                break;

            case R.id.btn_cancel:

                RequestParams cparams = new RequestParams();
                cparams.addBodyParameter("sessionid", ((ECApplication) getApplication()).sessionId);
                cparams.addBodyParameter("aid", String.valueOf(extras.getInt("id")));


                new HttpUtils().send(HttpRequest.HttpMethod.POST, CANCEL_ACTIVITY, cparams, new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        AfterCancel();
                        //Toast.makeText(MovementDetailActivity.this, "参加成功", Toast.LENGTH_SHORT).show();
                        //?
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });
                break;

            case R.id.partyImage:
                Intent intent = new Intent(MovementDetailActivity.this, BigImageActivity.class);
                intent.putExtra("URL", extras.getString("imageUrl"));
                startActivity(intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            MovementDetailActivity.this.finish();

        }

        return super.onKeyDown(keyCode, event);

    }


}
