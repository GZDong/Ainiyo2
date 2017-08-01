package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.adapter.ImageAdapter;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.ArrayList;


public class ModeAddingActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x00000011;

    private RecyclerView rvImage;
    private ImageAdapter mAdapter;
    private Button btn_limit;
    private int requestCode;
    private ArrayList<String> images;
    @ViewInject(R.id.et_mode_add_saying)
    EditText et_mode_add_saying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_adding);

        ViewUtils.inject(this);

        rvImage = (RecyclerView) findViewById(R.id.rv_image);
        rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ImageAdapter(this);
        rvImage.setAdapter(mAdapter);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            mAdapter.refresh(images);
        }
    }


    @OnClick({R.id.mode_adding_back,R.id.mode_add_pic,R.id.tv_mode_add})
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.mode_adding_back:
                startActivity(new Intent(ModeAddingActivity.this,MainActivity.class));
                break;
            case R.id.mode_add_pic:
                ImageSelectorUtils.openPhoto(ModeAddingActivity.this, REQUEST_CODE, false, 9);
                break;
            case R.id.tv_mode_add:
                Intent t1=new Intent();
                t1.putStringArrayListExtra("images", images);
                t1.putExtra("text",et_mode_add_saying.getText().toString());
                setResult(2,t1);
                finish();
                break;

        }
    }

}
