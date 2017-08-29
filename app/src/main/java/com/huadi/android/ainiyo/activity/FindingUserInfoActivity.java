package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.huadi.android.ainiyo.frag.FlagFragment;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class FindingUserInfoActivity extends AppCompatActivity {

    private ListView mListView;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private FindingInfo fi;

    private Toolbar mToolbar;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_user_info);

        ViewUtils.inject(this);

        setImmersive();

        final Intent t = getIntent();
        fi = (FindingInfo) t.getSerializableExtra("findinginfo");
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(fi.getName());
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));

        initView();
    }

    public void setImmersive() {
        //设置状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle("个人信息");
        }

        mTextView = (TextView) findViewById(R.id.set_flag_text);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                FlagFragment flagFragment = FlagFragment.newInstance(fi.getName());
                flagFragment.show(fm, "Fri");
            }
        });

    }

    @OnClick(R.id.btn_finding_add_friend )
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finding_add_friend:
                Intent intent = new Intent(FindingUserInfoActivity.this, AddFriendActivity.class);
                intent.putExtra("name", fi.getName());
                startActivity(intent);
                break;
        }
    }
}
