package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditBirthActivity extends AppCompatActivity {
    @ViewInject(R.id.birth_layout)
    private TextInputLayout birth_layout;
    @ViewInject(R.id.next)
    private Button next;
    @ViewInject(R.id.birth)
    private EditText birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_birth);
        ViewUtils.inject(this);
        birth_layout.setHint("生日（比如1995-10-22）");
    }


    @OnClick({R.id.next})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.next:
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("birthday",birth.getText().toString());
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifybirthday", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                            if (msg.equals("success")) {
                                startActivity(new Intent(EditBirthActivity.this,EditSalaryActivity.class));
                                finish();
                            } else {

                                Toast.makeText(EditBirthActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(EditBirthActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
        }
    }
}
