package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.ActivityResult;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class ActivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        ImageView back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(ActivityActivity.this, MainActivity.class));
            }
        });

        RequestParams params=new RequestParams();
        params.addBodyParameter("sessionid",sessionId);
        params.addBodyParameter("page","1");
        params.addBodyParameter("pagesize","4");
        new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/getallactivity", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result.toString());
                    Gson gson=new Gson();
                    ActivityResult activityResults=gson.fromJson(object.getJSONObject("Result").toString(),ActivityResult.class);





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(ActivityActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
