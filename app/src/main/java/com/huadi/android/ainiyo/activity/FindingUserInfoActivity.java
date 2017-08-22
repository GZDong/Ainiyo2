package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class FindingUserInfoActivity extends AppCompatActivity {

    private List<String> mList;
    @ViewInject(R.id.ll_finding_userinfo)
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_user_info);

        ViewUtils.inject(this);


        initView();
    }


    public void initView() {
        mList = new ArrayList<>();
        String tag1 = "设置备注和标签";
        String tag2 = "兴趣爱好";
        String tag3 = "生活照";
        mList.add(tag1);
        mList.add(tag2);
        mList.add(tag3);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FindingUserInfoActivity.this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(adapter);
    }

    @OnClick({R.id.iv_finding_userinfo_back, R.id.btn_finding_add_friend})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finding_userinfo_back:
                finish();
                break;
            case R.id.btn_finding_add_friend:
                Intent intent = new Intent(FindingUserInfoActivity.this,AddFriendActivity.class);
                intent.putExtra("name","gaodd");
                startActivity(intent);
                break;
        }
    }
}
