package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.donkingliang.imageselector.entry.Image;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.ImageAdapter;
import com.huadi.android.ainiyo.entity.ModeInfo;

import java.util.ArrayList;

public class ModeDetailActivity extends AppCompatActivity {

    private RecyclerView rvImage;
    private ImageAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_detail);

        rvImage = (RecyclerView) findViewById(R.id.rv_mode_image);


        // 判断屏幕方向
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mLayoutManager = new GridLayoutManager(this, 5);
        }
        rvImage.setLayoutManager(mLayoutManager);


        mAdapter = new ImageAdapter(this);
        rvImage.setAdapter(mAdapter);
        final Intent intent=getIntent();
        ArrayList<String> mPhoList=intent.getStringArrayListExtra("item");
        mAdapter.refresh(mPhoList);

        ((SimpleItemAnimator) rvImage.getItemAnimator()).setSupportsChangeAnimations(false);

//        mAdapter.setOnImageSelectListener(new ImageAdapter.OnImageSelectListener() {
//            @Override
//            public void OnImageSelect(Image image, boolean isSelect, int selectCount) {
//                setSelectImageCount(selectCount);
//            }
//        });
        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String image, int position) {
                Intent t=new Intent(ModeDetailActivity.this,PreviewActivity.class);
                t.putExtra("image",image);
                startActivity(t);
            }
        });
    }

//    private void toPreviewActivity(String images, int position) {
//        if (images != null && !images.isEmpty()) {
//            PreviewActivity.openActivity(this, images,
//                    mAdapter.getSelectImages(), isSingle, mMaxCount, position);
//        }
//    }
}
