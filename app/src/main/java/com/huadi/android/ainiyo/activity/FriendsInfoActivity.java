package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhidong on 2017/8/5.
 */

public class FriendsInfoActivity extends AppCompatActivity {

    private int picture;
    private String name;
    private TextView mTextView;
    private Button mButton;
    private ImageView mImageView;
    private UserInfo mUserInfo;
    private Toolbar mToolbar;
    private ListView mListView;
    private List<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        picture = intent.getIntExtra("picture",0);
        mUserInfo = (UserInfo) intent.getSerializableExtra("userInfo");

        initView();

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle(getResources().getString(R.string.fri_info));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar2, menu);
        return true;
    }

    private void initView(){
        mTextView =(TextView) findViewById(R.id.NameTextView);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mButton = (Button) findViewById(R.id.send_msg);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mListView = (ListView) findViewById(R.id.list);

        mList = new ArrayList<>();
        String tag1 = "设置备注和标签";
        String tag2 = "兴趣爱好";
        String tag3 = "生活照";
        mList.add(tag1);
        mList.add(tag2);
        mList.add(tag3);

        mTextView.setText(name);
        mImageView.setImageResource(picture);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendsInfoActivity.this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(adapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FriendsInfoActivity.this, ChattingActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("img", picture);
                intent.putExtra("userInfo", mUserInfo);

                Friends fri = FriendsLab.get(FriendsInfoActivity.this, mUserInfo).getFriend(name);
                //****在本地置0*****
                fri.setUnreadMeg(0);
                //****在服务器端置0新信息****
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(name);
                if (conversation != null) {
                    conversation.markAllMessagesAsRead();
                }
                if (fri.isShowInChooseFragment() == false) {
                    fri.setShowInChooseFragment(true);
                }
                startActivity(intent);
            }
        });
    }


}
