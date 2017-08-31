package com.huadi.android.ainiyo.activity;

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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class EditMateSignActivity extends AppCompatActivity {

    @ViewInject(R.id.edit_mate_sign)
    EditText edit_mate_sign;
    @ViewInject(R.id.edit_mate_sign_text_num)
    TextView edit_mate_sign_text_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mate_sign);

        ViewUtils.inject(this);

        edit_mate_sign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                String content = edit_mate_sign.getText().toString();
                edit_mate_sign_text_num.setText(String.valueOf(50 - content.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick({R.id.edit_mate_sign_publish, R.id.edit_mate_sign_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_mate_sign_publish:
                if (!TextUtils.isEmpty(edit_mate_sign.getText())) {
                    finish();
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "要输入内容才可以保存哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edit_mate_sign_back:
                finish();
                break;
        }
    }

}
