package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.AreaData;
import com.huadi.android.ainiyo.entity.UserData;
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

import java.io.File;

import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class KidActivity extends AppCompatActivity {

    @ViewInject(R.id.yes_select)
    private ImageView yes_select;
    @ViewInject(R.id.no_select)
    private ImageView no_select;
    @ViewInject(R.id.yes)
    private LinearLayout yes;
    @ViewInject(R.id.no)
    private LinearLayout no;
    @ViewInject(R.id.back)
    private ImageView back;


    private Boolean situation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        //获取用户详细信息//

        ViewUtils.inject(this);
        Intent intent=getIntent();
        situation=intent.getBooleanExtra("parent",false);
        if(situation){
            yes_select.setVisibility(View.VISIBLE);
        }
        if(!situation){
            no_select.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.yes,R.id.no,R.id.back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(KidActivity.this, InfoActivity.class));
                break;
            case R.id.yes:
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("gentle", "1");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifyhavekids", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                            if (msg.equals("success")) {
                                Toast.makeText(KidActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(KidActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(KidActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });
                finish();
                break;
            case R.id.no:
                RequestParams params1 = new RequestParams();
                params1.addBodyParameter("sessionid", sessionId);
                params1.addBodyParameter("gentle", "2");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifyhavekids", params1, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                            if (msg.equals("success")) {
                                Toast.makeText(KidActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(KidActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(KidActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });
                finish();
                break;
        }
    }




}
