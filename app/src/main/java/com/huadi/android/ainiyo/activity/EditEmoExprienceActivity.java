package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class EditEmoExprienceActivity extends AppCompatActivity {

    @ViewInject(R.id.et_emotion_experience)
    EditText et_emotion_experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emo_exprience);

        ViewUtils.inject(this);
        Intent intent=getIntent();
        String text=intent.getStringExtra("emotion");
        et_emotion_experience.setText(text);
    }

    @OnClick({R.id.emotion_experience_publish, R.id.emotion_experience_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emotion_experience_publish:
                if (TextUtils.isEmpty(et_emotion_experience.getText())) {

                    Toast.makeText(this, "要输入内容才可以保存哦", Toast.LENGTH_SHORT).show();
                }


                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("emotion", et_emotion_experience.getText().toString());
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifyemotion", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                                finish();

                                Toast.makeText(EditEmoExprienceActivity.this, msg, Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        finish();

                        Toast.makeText(EditEmoExprienceActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
            case R.id.emotion_experience_back:
                finish();
                break;
        }
    }
}
