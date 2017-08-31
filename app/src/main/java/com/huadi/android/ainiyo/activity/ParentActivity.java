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

public class ParentActivity extends AppCompatActivity {
    private boolean situation;
    @ViewInject(R.id.alive_select)
    private ImageView alive_select;
    @ViewInject(R.id.dead_select)
    private ImageView dead_select;
    @ViewInject(R.id.alive)
    private LinearLayout alive;
    @ViewInject(R.id.dead)
    private LinearLayout dead;
    @ViewInject(R.id.back)
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        ViewUtils.inject(this);
        Intent intent = getIntent();
        situation = intent.getBooleanExtra("parent", false);
        if (situation) {
            alive_select.setVisibility(View.VISIBLE);
        }
        if (!situation) {
            dead_select.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.alive, R.id.dead, R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.alive://在世
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("parentsalive", "1");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifyparentsalive", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");

                            startActivity(new Intent(ParentActivity.this, InfoActivity.class));
                            Toast.makeText(ParentActivity.this, msg, Toast.LENGTH_SHORT).show();
                            finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(ParentActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
            case R.id.dead://不在世
                RequestParams params1 = new RequestParams();
                params1.addBodyParameter("sessionid", sessionId);
                params1.addBodyParameter("parentsalive", "2");
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifyparentsalive", params1, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");

                            startActivity(new Intent(ParentActivity.this, InfoActivity.class));
                            Toast.makeText(ParentActivity.this, msg, Toast.LENGTH_SHORT).show();

                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(ParentActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
        }
    }
}
