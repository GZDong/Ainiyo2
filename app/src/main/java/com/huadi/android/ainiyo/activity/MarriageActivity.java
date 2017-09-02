package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class MarriageActivity extends AppCompatActivity {
    private String situation;
    @ViewInject(R.id.notyet_select)
    private ImageView notyet_select;
    @ViewInject(R.id.already_select)
    private ImageView already_select;
    @ViewInject(R.id.notyet)
    private LinearLayout notyet;
    @ViewInject(R.id.already)
    private LinearLayout already;
    @ViewInject(R.id.back)
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marriage);
        ViewUtils.inject(this);
        Intent intent = getIntent();
        situation = intent.getStringExtra("marriage");
        if (situation.equals("未婚")) {
            notyet_select.setVisibility(View.VISIBLE);
        }
        if (situation.equals("已婚")) {
            already_select.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.notyet, R.id.already, R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.notyet:
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("maritallstatus", "未婚");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifymaritallstatus", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");

                            Intent intent=new Intent();
                            intent.putExtra("data_return","未婚");
                            setResult(RESULT_OK,intent);
                            finish();
                            Toast.makeText(MarriageActivity.this, msg, Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(MarriageActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
            case R.id.already:
                RequestParams params1 = new RequestParams();
                params1.addBodyParameter("sessionid", sessionId);
                params1.addBodyParameter("maritallstatus", "已婚");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifymaritallstatus", params1, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");

                            Intent intent=new Intent();
                            intent.putExtra("data_return","已婚");
                            setResult(RESULT_OK,intent);
                            finish();
                            Toast.makeText(MarriageActivity.this, msg, Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(MarriageActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
        }
    }
}
