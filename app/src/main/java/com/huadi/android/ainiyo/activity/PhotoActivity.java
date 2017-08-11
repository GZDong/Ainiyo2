package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.lidroid.xutils.ViewUtils;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {
    @ViewInject(R.id.image)
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.image})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                ImageSelectorUtils.openPhoto(PhotoActivity.this, 1, false, 9);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(
                    ImageSelectorUtils.SELECT_RESULT);
        }
    }


}
