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

public class EditHobbyActivity extends AppCompatActivity {

    @ViewInject(R.id.edit_hobby)
    EditText edit_hobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hobby);

        ViewUtils.inject(this);
    }

    @OnClick({R.id.edit_hobby_publish, R.id.edit_hobby_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_hobby_publish:
                if (!TextUtils.isEmpty(edit_hobby.getText())) {
                    finish();
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "要输入内容才可以保存哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edit_hobby_back:
                finish();
                break;
        }
    }
}
