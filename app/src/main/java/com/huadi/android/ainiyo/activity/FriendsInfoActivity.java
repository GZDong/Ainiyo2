package com.huadi.android.ainiyo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.huadi.android.ainiyo.util.DateUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Date;
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
    private TextView sexAndageText;
    private ImageView sexImage;
    private TextView signText;


    private Button mChangeBtn;
    private CardView mFriCard;
    private Button mSendMsg;

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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huadi.android.ainiyo.refreshName");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCollapsingToolbarLayout.setTitle(intent.getStringExtra("NewName"));
            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
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
        sexAndageText = (TextView) findViewById(R.id.sex_and_age);
        sexImage = (ImageView) findViewById(R.id.sex_image);
        signText = (TextView) findViewById(R.id.signField);

        mSendMsg = (Button) findViewById(R.id.send_btn);
        if (name.equals(UserInfoLab.get(FriendsInfoActivity.this).getUserInfo().getUsername())){
            mChangeBtn.setVisibility(View.VISIBLE);
            mFriCard.setVisibility(View.GONE);
            mFloatBtn.setVisibility(View.GONE);
            mSendMsg.setVisibility(View.GONE);
            if (UserInfoLab.get(this).getUserInfo().getAreaName()!=null) {
                areaText.setText(String.valueOf(UserInfoLab.get(this).getUserInfo().getAreaName()));
            }else {
                areaText.setText("-");
            }
            if (!TextUtils.isEmpty( UserInfoLab.get(this).getUserInfo().getSign())){
                signText.setText(UserInfoLab.get(this).getUserInfo().getSign());
            } else {
                signText.setTextColor(getResources().getColor(R.color.little_gray));
                signText.setText("你还没有设置签名");
            }
            String sex = null;
            String age = null;

            if (UserInfoLab.get(this).getUserInfo().getSex()!=null) {
                if (UserInfoLab.get(this).getUserInfo().getSex().equals("0")){
                    sex = "男";
                    sexImage.setImageResource(R.drawable.boy2);
                }else if (UserInfoLab.get(this).getUserInfo().getSex().equals("1")){
                    sex = "男";
                    sexImage.setImageResource(R.drawable.boy2);
                }else if (UserInfoLab.get(this).getUserInfo().getSex().equals("2")){
                    sex = "女";
                    sexImage.setImageResource(R.drawable.girl2);
                }
            }else {
                sex = "男";
                sexImage.setImageResource(R.drawable.boy2);
            }

            if (UserInfoLab.get(this).getUserInfo().getBirthday()!=null){
                String subStr = UserInfoLab.get(this).getUserInfo().getBirthday().substring(0,4);
                int diff = Integer.valueOf(DateUtil.getYear())-Integer.valueOf(subStr);
                age = String.valueOf(diff);
            }else {
                age = "-";
            }
            sexAndageText.setText(sex + " " + age);


            if (UserInfoLab.get(this).getUserInfo().getPhone()!=null) {
                phoneText.setText(UserInfoLab.get(this).getUserInfo().getPhone());
            } else {
                phoneText.setText("-");
            }
        }else {
            if (FriendsLab.get(this).getFriend(name).getAreaName()!= null) {
                areaText.setText(FriendsLab.get(this).getFriend(name).getAreaName());
            }else {
                areaText.setText("-");
            }
            if (!TextUtils.isEmpty(FriendsLab.get(this).getFriend(name).getSign())){
                signText.setText(FriendsLab.get(this).getFriend(name).getSign());
            } else {
                signText.setTextColor(getResources().getColor(R.color.little_gray));
                signText.setText("该好友还没有设置签名");
            }
            String sex = null;
            String age = null;

            if (FriendsLab.get(this).getFriend(name).getSex()!=null) {
                if (FriendsLab.get(this).getFriend(name).getSex().equals("0")) {
                    sex = "男";
                    sexImage.setImageResource(R.drawable.boy2);
                } else if (FriendsLab.get(this).getFriend(name).getSex().equals("1")) {
                    sex = "男";
                    sexImage.setImageResource(R.drawable.boy2);
                } else if (FriendsLab.get(this).getFriend(name).getSex().equals("2")) {
                    sex = "女";
                    sexImage.setImageResource(R.drawable.girl2);
                }
            }else {
                sex = "女";
                sexImage.setImageResource(R.drawable.girl2);
            }
                if (FriendsLab.get(this).getFriend(name).getBirthday()!=null){
                    String subStr = FriendsLab.get(this).getFriend(name).getBirthday().substring(0,4);
                    int diff = Integer.valueOf(DateUtil.getYear())-Integer.valueOf(subStr);
                    age = String.valueOf(diff);
                }else {
                    age = "-";
                }

            sexAndageText.setText(sex + " " + age);


            if (FriendsLab.get(this).getFriend(name).getPhone()!=null) {
                phoneText.setText(FriendsLab.get(this).getFriend(name).getPhone());
            } else {
                phoneText.setText("-");
            }
        }
        //**********

        //***************


        if (!TextUtils.isEmpty(phoneText.getText())) {
            phoneText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = getSupportFragmentManager();
                    PhoneFragment fragment = PhoneFragment.newInstance(phoneText.getText().toString());
                    fragment.show(fm, "Phone");
                }
            });
        }


        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(name.equals(UserInfoLab.get(this).getUserInfo().getUsername())){
            mCollapsingToolbarLayout.setTitle(name);
        }else {
            if (!TextUtils.isEmpty(FriendsLab.get(this).getFriend(name).getTagMsg())){
                mCollapsingToolbarLayout.setTitle(FriendsLab.get(this).getFriend(name).getTagMsg());
            }else {
                mCollapsingToolbarLayout.setTitle(name);
            }
        }
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.gray));
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
                    HobbyFragment hobbyFragment = HobbyFragment.newInstance(name);
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

        mSendMsg.setOnClickListener(new View.OnClickListener() {
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
                startActivity(new Intent(FriendsInfoActivity.this, InfoActivity.class));
            }
        });
    }


}
