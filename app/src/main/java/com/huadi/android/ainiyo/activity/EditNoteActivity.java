package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
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

public class EditNoteActivity extends AppCompatActivity {

    @ViewInject(R.id.edit_note)
    EditText edit_note;
    @ViewInject(R.id.edit_note_text_num)
    TextView edit_note_text_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        ViewUtils.inject(this);

        edit_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                String content = edit_note.getText().toString();
                edit_note_text_num.setText(String.valueOf(50 - content.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Intent intent = getIntent();
        String text = intent.getStringExtra("note");
        edit_note.setText(text);
    }

    @OnClick({R.id.edit_note_publish, R.id.edit_note_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_note_publish:
                if (TextUtils.isEmpty(edit_note.getText())) {

                    Toast.makeText(this, "要输入内容才可以保存哦", Toast.LENGTH_SHORT).show();
                }
                UserInfoLab.get(EditNoteActivity.this).ChangeSign(edit_note.getText().toString());

                RequestParams params = new RequestParams();
                params.addBodyParameter("sessionid", sessionId);
                params.addBodyParameter("autograph", edit_note.getText().toString());
                new HttpUtils().send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/modifyautograph", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject object = new JSONObject(responseInfo.result.toString());
                            int status = object.getInt("Status");
                            String result = object.getString("Result");
                            String msg = object.getString("Msg");
                            startActivity(new Intent(EditNoteActivity.this, InfoActivity.class));

                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {


                        Toast.makeText(EditNoteActivity.this, "连接错误", Toast.LENGTH_SHORT).show();

                    }
                });

                break;
            case R.id.edit_note_back:
                finish();
                break;
        }
    }

}
