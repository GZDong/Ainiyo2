package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.adapter.ImageAdapter;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

public class ModeDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.rv_mode_image)
    private RecyclerView rvImage;

    private ImageAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_detail);


        ViewUtils.inject(this);
        setImmersive();

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
                Intent t=new Intent(ModeDetailActivity.this,ModePreviewActivity.class);
                t.putExtra("image",image);
                startActivity(t);
            }
        });
    }

    public void setImmersive() {
        //设置状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.status_bar_mode_detail);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = ToolKits.getStatusBarHeight(this);
            //动态的设置隐藏布局的高度
            linear_bar.getLayoutParams().height = statusHeight;
        }
    }


    @OnClick({R.id.mode_detail_back})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.mode_detail_back:
                ToolKits.putInt(this,"fragment",2);
                finish();
                break;
        }
    }

    //按返回键时回到相应的fragment
    @Override
    public void onBackPressed()
    {
        ToolKits.putInt(this,"fragment",2);
        super.onBackPressed();
    }


}
