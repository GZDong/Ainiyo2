package com.huadi.android.ainiyo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ComplainActivity extends AppCompatActivity {

    @ViewInject(R.id.et_version_complain)
    EditText et_version_complain;
    @ViewInject(R.id.version_complain_text_num)
    TextView version_complain_text_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        ViewUtils.inject(this);

        et_version_complain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                String content = et_version_complain.getText().toString();
                version_complain_text_num.setText(String.valueOf(50 - content.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.version_complain_publish, R.id.version_complain_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.version_complain_publish:
                if (!TextUtils.isEmpty(et_version_complain.getText())) {
                    finish();
                    Toast.makeText(this, "谢谢您的反馈，我们会尽快处理", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "要输入内容才可以发表哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.version_complain_back:
                finish();
                break;
        }
    }
}
