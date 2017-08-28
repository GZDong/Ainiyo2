package com.huadi.android.ainiyo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Long2;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.frag.FlagFragment;
import com.huadi.android.ainiyo.frag.HobbyFragment;
import com.huadi.android.ainiyo.frag.PhoneFragment;
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
    private FloatingActionButton mFloatBtn;
    private ImageView mImageView;
    private UserInfo mUserInfo;
    private Toolbar mToolbar;
    private ListView mListViewForOther;
    private List<String> mList;
    private String from;
    private TextView areaText;
    private TextView phoneText;
    private Button mChangeBtn;
    private CardView mFriCard;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fir_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    localLayoutParams.flags);
        }

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        picture = intent.getIntExtra("picture",0);
        mUserInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        from = intent.getStringExtra("from");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (from.equals("ChattingFragment")) {
                    Intent intent = new Intent(FriendsInfoActivity.this, ChattingActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("img", picture);
                    intent.putExtra("userInfo", mUserInfo);
                    startActivity(intent);
                } else if (from.equals("FriendsListActivity")) {
                    Intent intent = new Intent(FriendsInfoActivity.this, FriendsListActivity.class);
                    intent.putExtra("userInfo", mUserInfo);
                    startActivity(intent);
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){

        mFriCard = (CardView) findViewById(R.id.friend_card);
        mChangeBtn = (Button) findViewById(R.id.change_userinfo);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mFloatBtn = (FloatingActionButton) findViewById(R.id.send_msg);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mListViewForOther = (ListView) findViewById(R.id.list);
        areaText = (TextView) findViewById(R.id.area_text);
        phoneText = (TextView) findViewById(R.id.phone_text);
        if (name.equals(UserInfoLab.get(FriendsInfoActivity.this).getUserInfo().getUsername())){
            mChangeBtn.setVisibility(View.VISIBLE);
            mFriCard.setVisibility(View.GONE);
            mFloatBtn.setVisibility(View.GONE);
        }
        phoneText.setText("13322223344");
        phoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                PhoneFragment fragment = PhoneFragment.newInstance(phoneText.getText().toString());
                fragment.show(fm, "Phone");
            }
        });

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(name);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));

        mList = new ArrayList<>();
        String tag1 = "设置备注和标签";
        String tag2 = "兴趣爱好";
        String tag3 = "生活照";
        mList.add(tag1);
        mList.add(tag2);
        mList.add(tag3);


        if (!name.equals(UserInfoLab.get(this).getUserInfo().getUsername())) {
            if (!TextUtils.isEmpty(FriendsLab.get(this, mUserInfo).getFriend(name).getPicUrl())) {
                Glide.with(this).load(FriendsLab.get(this, mUserInfo).getFriend(name).getPicUrl()).into(mImageView);
            } else {
                mImageView.setImageResource(picture);
            }
        }else{
            if (!TextUtils.isEmpty(UserInfoLab.get(this,mUserInfo).getUserInfo().getPicUrl())){
                Glide.with(this).load(UserInfoLab.get(this).getUserInfo().getPicUrl()).into(mImageView);
            }else {
                mImageView.setImageResource(picture);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FriendsInfoActivity.this, android.R.layout.simple_list_item_1, mList);
        mListViewForOther.setAdapter(adapter);


        mListViewForOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mList.get(position).equals("设置备注和标签")) {
                    FragmentManager fm = getSupportFragmentManager();
                    FlagFragment flagFragment = FlagFragment.newInstance(name);
                    flagFragment.show(fm, "Fri");
                }
                if (mList.get(position).equals("兴趣爱好")) {
                    FragmentManager fm = getSupportFragmentManager();
                    HobbyFragment hobbyFragment = new HobbyFragment();
                    hobbyFragment.show(fm, "Hob");
                }
                if (mList.get(position).equals("生活照")) {
                    Intent intent = new Intent(FriendsInfoActivity.this, PhoneWallActivity.class);
                    startActivity(intent);
                }
            }
        });
        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FriendsInfoActivity.this, ChattingActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("img", picture);
                intent.putExtra("userInfo", mUserInfo);

                FriendsLab.get(FriendsInfoActivity.this, mUserInfo).clearUnread(name);

                //****在服务器端置0新信息****
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(name);
                if (conversation != null) {
                    conversation.markAllMessagesAsRead();
                }
                if (FriendsLab.get(FriendsInfoActivity.this, mUserInfo).getFriend(name).isShowInChooseFragment() == false) {
                    FriendsLab.get(FriendsInfoActivity.this, mUserInfo).addNewDialog(name);
                }
                startActivity(intent);
            }
        });
        mChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendsInfoActivity.this,EditInfoActivity.class));
            }
        });
    }


}
