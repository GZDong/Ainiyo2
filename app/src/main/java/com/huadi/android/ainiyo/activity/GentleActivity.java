package com.huadi.android.ainiyo.activity;

import android.content.Intent;
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

public class GentleActivity extends AppCompatActivity {

    @ViewInject(R.id.male_select)
    private ImageView male_select;
    @ViewInject(R.id.female_select)
    private ImageView female_select;
    @ViewInject(R.id.male)
    private LinearLayout male;
    @ViewInject(R.id.female)
    private LinearLayout female;
    @ViewInject(R.id.back)
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        ViewUtils.inject(this);
        Intent intent = getIntent();
        String situation = intent.getStringExtra("sex");
        if (situation.equals("1")) {
            male_select.setVisibility(View.VISIBLE);
        }
        if (situation.equals("2")) {
            female_select.setVisibility(View.VISIBLE);
        }

    }


    @OnClick({R.id.male, R.id.female, R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.male:
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("gentle", "1");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifygentle", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                           Intent intent=new Intent();
                            intent.putExtra("data_return","男");
                                    setResult(RESULT_OK,intent);
                            finish();
                                Toast.makeText(GentleActivity.this, msg, Toast.LENGTH_SHORT).show();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {


                        Toast.makeText(GentleActivity.this, "连接错误", Toast.LENGTH_SHORT).show();


                    }
                });

                break;
            case R.id.female:
                RequestParams params1 = new RequestParams();
                params1.addBodyParameter("sessionid", sessionId);
                params1.addBodyParameter("gentle", "2");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifygentle", params1, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                            Intent intent=new Intent();
                            intent.putExtra("data_return","女");
                            setResult(RESULT_OK,intent);
                            finish();
                            Toast.makeText(GentleActivity.this, msg, Toast.LENGTH_SHORT).show();




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        finish();

                        Toast.makeText(GentleActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });
                finish();
                break;
        }
    }


}


