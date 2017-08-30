package com.huadi.android.ainiyo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class EditEmoExprienceActivity extends AppCompatActivity {

    @ViewInject(R.id.et_emotion_experience)
    EditText et_emotion_experience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emo_exprience);

        ViewUtils.inject(this);
    }

    @OnClick({R.id.emotion_experience_publish, R.id.emotion_experience_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emotion_experience_publish:
                if (!TextUtils.isEmpty(et_emotion_experience.getText())) {
                    finish();
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "要输入内容才可以保存哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.emotion_experience_back:
                finish();
                break;
        }
    }
}
