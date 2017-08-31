package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
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

public class EditSexActivity extends AppCompatActivity {
    @ViewInject(R.id.next)
    private Button next;
    @ViewInject(R.id.male)
    private RadioButton male;
    @ViewInject(R.id.female)
    private RadioButton female;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sex);
        ViewUtils.inject(this);

    }

    @OnClick({R.id.next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:

                if (male.isChecked()) {
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
                                if (msg.equals("success")) {

                                    startActivity(new Intent(EditSexActivity.this, MainActivity.class));
                                    finish();


                                } else {

                                    Toast.makeText(EditSexActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {

                            Toast.makeText(EditSexActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                        }
                    });
                    finish();
                    break;
                } else {
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("sessionid", sessionId);
                    params.addBodyParameter("gentle", "2");
                    new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifygentle", params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            try {
                                JSONObject object = new JSONObject(responseInfo.result.toString());
                                int status = object.getInt("Status");
                                String result = object.getString("Result");
                                String msg = object.getString("Msg");
                                if (msg.equals("success")) {
                                    startActivity(new Intent(EditSexActivity.this, MainActivity.class));
                                    finish();
                                } else {

                                    Toast.makeText(EditSexActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {

                            Toast.makeText(EditSexActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                        }
                    });

                    break;
                }
        }
    }
}
