package com.huadi.android.ainiyo;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.frag.AddressBookFragment;
import com.huadi.android.ainiyo.frag.ChooseFragment;
import com.huadi.android.ainiyo.frag.FindingFragment;
import com.huadi.android.ainiyo.frag.MeFragment;
import com.huadi.android.ainiyo.frag.ModeFragment;
import com.huadi.android.ainiyo.util.SignInUtil;
import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnCheckedChange;


public class MainActivity extends AppCompatActivity {

    public static String mChatId ;
    // 当前会话对象
    private static int mImage;
    //private EMConversation mConversation;
    private static String mPasswd;

    @ViewInject(R.id.bottom_bar)
    private RadioGroup bottom_bar;
    @ViewInject(R.id.layout_content)
    private FrameLayout layout_content;
    private boolean isInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,"android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"},1);
        }


        ViewUtils.inject(this);

        ActionBar actionBar = getSupportActionBar();

        /*try{
            actionBar.setTitle(mChatId);
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/

        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        //  mMessageListener = this;
        /*FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.layout_content);
        if (fragment == null) {
            // fragment = ChooseFragment.newInstance();
            fragment = ChattingFragment.newInstance(mChatId,mImage);
            fm.beginTransaction().add(R.id.layout_content,fragment).commit();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this,"你拒绝了程序正常运行需要的权限!",Toast.LENGTH_LONG);
                }
        }
    }

    protected void onStart(){
        super.onStart();
        bottom_bar.check(R.id.radio0);
    }

    @OnCheckedChange({R.id.bottom_bar})
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = 0;
        switch (checkedId) {
            case R.id.radio0:
                index = 0;
                break;
            case R.id.radio1:
                index = 1;
                break;
            case R.id.radio2:
                index = 2;
                break;
            case R.id.radio3:
                index = 3;
                break;
            case R.id.radio4:
                index = 4;
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
                    fragment =new AddressBookFragment();
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

}
