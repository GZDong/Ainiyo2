package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
                startActivity(new Intent(EditBirthActivity.this,EditSalaryActivity.class));
                //如果点击继续，则保存信息
                finish();
        }
    }
}
