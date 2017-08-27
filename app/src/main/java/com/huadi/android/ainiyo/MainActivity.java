package com.huadi.android.ainiyo;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



import com.huadi.android.ainiyo.Retrofit2.GetRequset_check_Interface;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_login_Interface;
import com.huadi.android.ainiyo.activity.ChooseYoNActivity;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.frag.MovementFragment;
import com.huadi.android.ainiyo.frag.ChooseFragment;
import com.huadi.android.ainiyo.frag.FindingFragment;
import com.huadi.android.ainiyo.frag.MeFragment;
import com.huadi.android.ainiyo.frag.ModeFragment;
import com.huadi.android.ainiyo.gson.ResultForCheck;
import com.huadi.android.ainiyo.gson.ResultForLogin;
import com.huadi.android.ainiyo.util.ToolKits;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnCheckedChange;

import com.huadi.android.ainiyo.adapter.MyFragmentPagerAdapter;

import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.lang.reflect.Field;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener,EMContactListener{

    /*public static String mChatId ;
    // 当前会话对象
    private static int mImage;
    //private EMConversation mConversation;
    private static String mPasswd;*/

    @ViewInject(R.id.bottom_bar)
    private RadioGroup bottom_bar;
    @ViewInject(R.id.layout_content)
    private FrameLayout layout_content;
    private boolean isInit = false;

    private RadioButton button0;
    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private RadioButton button4;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_FIVE = 4;

    private MyFragmentPagerAdapter mAdapter;
    private ViewPager vpager;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Connector.getDatabase();

        EMClient.getInstance().contactManager().setContactListener(this);

        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        username=pref.getString("name","");
        password=pref.getString("pwd","");

        UserInfo userInfo = new UserInfo(username,password);
        UserInfoLab.get(MainActivity.this,userInfo);
//      Log.e("test","onMainActivity" + userInfo.getUsername()+UserInfoLab.get(MainActivity.this).getUserInfo().getUsername());

        ViewUtils.inject(this);

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("test","onDestroy_MainActivity");

        //每次退出都重置允许下次finding页面的tips
        SharedPreferences pref = this.getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFirstTime", true);
        editor.apply();

        EMClient.getInstance().contactManager().removeContactListener(this);
    }

    private void bindViews() {
        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
        button0 = (RadioButton)findViewById(R.id.radio0);
        button1 = (RadioButton)findViewById(R.id.radio1);
        button2 = (RadioButton)findViewById(R.id.radio2);
        button3 = (RadioButton)findViewById(R.id.radio3);
        button4 = (RadioButton)findViewById(R.id.radio4);

        setImmersive();
    }

    public void setImmersive() {
        //设置状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.status_bar_main);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = ToolKits.getStatusBarHeight(this);
            //动态的设置隐藏布局的高度
            linear_bar.getLayoutParams().height = statusHeight;
        }
    }


    @OnCheckedChange({R.id.bottom_bar})
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        int index = 0;
        switch (checkedId) {
            case R.id.radio0:
                index = PAGE_ONE;
                vpager.setCurrentItem(PAGE_ONE);
                button0.setTextSize(15);
                button1.setTextSize(0);
                button2.setTextSize(0);
                button3.setTextSize(0);
                button4.setTextSize(0);
                break;
            case R.id.radio1:
                index = PAGE_TWO;
                vpager.setCurrentItem(PAGE_TWO);
                button0.setTextSize(0);
                button1.setTextSize(15);
                button2.setTextSize(0);
                button3.setTextSize(0);
                button4.setTextSize(0);
                break;
            case R.id.radio2:
                index = PAGE_THREE;
                vpager.setCurrentItem(PAGE_THREE);
                button0.setTextSize(0);
                button1.setTextSize(0);
                button2.setTextSize(15);
                button3.setTextSize(0);
                button4.setTextSize(0);
                break;
            case R.id.radio3:
                index = PAGE_FOUR;
                vpager.setCurrentItem(PAGE_FOUR);
                button0.setTextSize(0);
                button1.setTextSize(0);
                button2.setTextSize(0);
                button3.setTextSize(15);
                button4.setTextSize(0);
                break;
            case R.id.radio4:
                index = PAGE_FIVE;
                vpager.setCurrentItem(PAGE_FIVE);
                button0.setTextSize(0);
                button1.setTextSize(0);
                button2.setTextSize(0);
                button3.setTextSize(0);
                button4.setTextSize(15);
                break;


        }
        Fragment fragment = (Fragment) fragments.instantiateItem(layout_content, index);
        fragments.setPrimaryItem(layout_content, 0, fragment);
        fragments.finishUpdate(layout_content);
    }

    protected void onResume() {
        super.onResume();
        if(!isInit){
            bottom_bar.check(R.id.radio0);
            isInit = true;
        }

    }

    protected void onPause() {
        super.onPause();
    };

    FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment = null;
            switch (arg0) {
                case 0://心情
                    fragment =new FindingFragment();
                    break;
                case 1://我的
                    fragment =new ChooseFragment();
                    break;
                case 2://我的
                    fragment =new ModeFragment();
                    break;
                case 3://我的
                    fragment = new MovementFragment();
                    break;
                case 4://我的
                    fragment =new MeFragment();
                    break;
                default:
                    new FindingFragment();
                    break;
            }
            return fragment;
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    button0.setChecked(true);
                    break;
                case PAGE_TWO:
                    button1.setChecked(true);
                    break;
                case PAGE_THREE:
                    button2.setChecked(true);
                    break;
                case PAGE_FOUR:
                    button3.setChecked(true);
                    break;
                case PAGE_FIVE:
                    button4.setChecked(true);
                    break;
            }
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            new AlertDialog.Builder(MainActivity.this).setTitle("提示")
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setMessage("确定要退出吗?")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create().show();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            // 监控菜单键
            Toast.makeText(MainActivity.this, "Menu", Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    //***************监听好友管理*******************
    //收到好友邀请
    @Override
    public void onContactInvited(String username, String reason) {
        //发送一条通知，附上对方姓名和添加理由，点击通知后跳转到新界面，在新界面决定是否同意添加
        Log.e("test","_______onContactInvited"  + reason);
        Intent intent = new Intent(MainActivity.this, ChooseYoNActivity.class);
        intent.putExtra("id",username);
        intent.putExtra("reason",reason);
        PendingIntent pi = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(MainActivity.this)
                .setContentTitle(username+"请求添加你为好友")
                .setContentText(reason)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_noti_big))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();
        manager.notify(1,notification);
    }
    //好友请求被同意
    @Override
    public void onFriendRequestAccepted(String username) {
        //把该好友的username上传到自己的服务器，然后加入数据库
        //调整自己服务器的接口，添加好友不需要附加信息
        Log.e("test","onFriendRequestAccepted______");
        Friends friends = new Friends(UserInfoLab.get(MainActivity.this).getUserInfo().getUsername(),username);
        FriendsLab.get(MainActivity.this,UserInfoLab.get(MainActivity.this).getUserInfo()).addFriend(friends);
        Toast.makeText(getApplication(),"对方同意接受你为好友",Toast.LENGTH_LONG).show();
    }

    //被删除时回调此方法
    @Override
    public void onContactDeleted(String username) {
        //被删除时，从2个服务器删除好友关系
        //第三方删除 EMClient.getInstance().contactManager().deleteContact(username);
        //删除本地数据库内容
        //告诉用户被删除好友了
    }
    //增加了联系人时回调此方法
    @Override
    public void onContactAdded(String username) {
        //点击同意后，上传服务器，更新本地数据库和单例
        //刷新好友列表
        Log.e("test","onContactAdded______");
        Toast.makeText(getApplication(),"联系人增加了",Toast.LENGTH_LONG).show();
    }
    //好友请求被拒绝
    @Override
    public void onFriendRequestDeclined(String username) {
        //发送一条通知，告诉好友请求被拒绝了
        Log.e("test","onFriendRequestDeclined______");
        Toast.makeText(getApplication(),"好友请求被拒绝了",Toast.LENGTH_LONG).show();
    }
}

