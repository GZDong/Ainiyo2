package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
                startActivity(new Intent(EditSexActivity.this, MainActivity.class));
                finish();
        }
    }
}
