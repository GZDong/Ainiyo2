package com.huadi.android.ainiyo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.AreaData;
import com.huadi.android.ainiyo.entity.UserData;
import com.lidroid.xutils.HttpUtils;
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

public class SexActivity extends AppCompatActivity {

    @ViewInject(R.id.male_select)
    private ImageView male_select;
    @ViewInject(R.id.female_select)
    private ImageView female_select;
    @ViewInject(R.id.male)
    private LinearLayout male;
    @ViewInject(R.id.female)
    private LinearLayout female;




    private int Gentle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        //获取用户详细信息//
        RequestParams params = new RequestParams();
        params.addBodyParameter("sessionid", sessionId);
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/getuserinfo", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInFo) {
                String info = responseInFo.result.toString();
                try {
                    JSONObject object = new JSONObject(info);
                    String msg = object.getString("Msg");
                    if(msg.equals("success")) {
                        Gson gson = new Gson();
                        UserData userData = gson.fromJson(object.getJSONObject("Result").toString(), UserData.class);
                        Gentle = userData.getGentle();
                    }


                } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException error, String msg) {
                                    Toast.makeText(SexActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                                }
                            });
        if(Gentle==1){
            //男右边的勾出来
            male_select.setVisibility(View.VISIBLE);
        }
        if(Gentle==2){
            //女右边的勾出来
            female_select.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.male,R.id.female})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.male:
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("avatar", "1");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifygentle", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                            if (msg.equals("success")) {
                                Toast.makeText(SexActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(SexActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(SexActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });
                finish();
                break;
            case R.id.female:
                RequestParams params1 = new RequestParams();
                params1.addBodyParameter("sessionid", sessionId);
                params1.addBodyParameter("avatar", "2");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifygentle", params1, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                            if (msg.equals("success")) {
                                Toast.makeText(SexActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(SexActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(SexActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });
                finish();
                break;
        }
    }




}


