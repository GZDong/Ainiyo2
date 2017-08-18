package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class VipHint extends AppCompatActivity {
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.next)
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_hint);
        ViewUtils.inject(this);
    }
    @OnClick({R.id.back,R.id.next})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(VipHint.this,MainActivity.class));
                break;
            case R.id.next:
                startActivity(new Intent(VipHint.this,VipApply.class));
                break;
        }
    }
}
