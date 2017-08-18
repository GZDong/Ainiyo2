package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;

public class BigImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        ImageView imageView = (ImageView) findViewById(R.id.big);
        Glide.with(BigImageActivity.this).load(url).placeholder(R.drawable.on).error(R.drawable.fail).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
