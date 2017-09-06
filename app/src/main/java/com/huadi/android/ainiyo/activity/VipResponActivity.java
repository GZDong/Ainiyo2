package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class VipResponActivity extends AppCompatActivity {
   @ViewInject(R.id.back)
   private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_respon);
        Intent intent = getIntent();
        String result = intent.getStringExtra("text");
        TextView textview = (TextView) findViewById(R.id.result_text);
        textview.setText(result);
        Button ok = (Button) findViewById(R.id.OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ok.getBackground().setAlpha(200);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
