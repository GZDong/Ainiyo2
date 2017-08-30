package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class BirthActivity extends AppCompatActivity {
    @ViewInject(R.id.birth_edit)
    private EditText birth_edit;
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.save)
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth);
        ViewUtils.inject(this);
        Intent intent=getIntent();
        String work=intent.getStringExtra("birth");
        birth_edit.setText(work);
    }
    @OnClick({R.id.back,R.id.save})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(BirthActivity.this,InfoActivity.class));
                break;
            case R.id.save:
                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("birthday", birth_edit.getText().toString());
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifybirthday", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                            if (msg.equals("success")) {
                                Toast.makeText(BirthActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(BirthActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                        Toast.makeText(BirthActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });
                finish();
                break;

        }
    }
}
