package com.huadi.android.ainiyo;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.huadi.android.ainiyo.frag.AddressBookFragment;
import com.huadi.android.ainiyo.frag.ChooseFragment;
import com.huadi.android.ainiyo.frag.FindingFragment;
import com.huadi.android.ainiyo.frag.MeFragment;
import com.huadi.android.ainiyo.frag.ModeFragment;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnCheckedChange;

import com.huadi.android.ainiyo.adapter.MyFragmentPagerAdapter;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,"android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"},1);
        }


        ViewUtils.inject(this);

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();


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

        //设置状态栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.status_bar_main);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            linear_bar.getLayoutParams().height = statusHeight;
        }
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
        int i= ToolKits.fetchInt(this,"fragment",0);

        switch (i)
        {
            case PAGE_ONE:
                bottom_bar.check(R.id.radio0);
                button0.setTextSize(15);
                button1.setTextSize(0);
                button2.setTextSize(0);
                button3.setTextSize(0);
                button4.setTextSize(0);
                break;
            case PAGE_TWO:
                vpager.setCurrentItem(PAGE_TWO);
                bottom_bar.check(R.id.radio1);
                button0.setTextSize(0);
                button1.setTextSize(15);
                button2.setTextSize(0);
                button3.setTextSize(0);
                button4.setTextSize(0);
                break;
            case PAGE_THREE:
                vpager.setCurrentItem(PAGE_THREE);
                bottom_bar.check(R.id.radio2);
                button0.setTextSize(0);
                button1.setTextSize(0);
                button2.setTextSize(15);
                button3.setTextSize(0);
                button4.setTextSize(0);
                break;
            case PAGE_FOUR:
                vpager.setCurrentItem(PAGE_FOUR);
                bottom_bar.check(R.id.radio3);
                button0.setTextSize(0);
                button1.setTextSize(0);
                button2.setTextSize(0);
                button3.setTextSize(15);
                button4.setTextSize(0);
                break;
            case PAGE_FIVE:
                vpager.setCurrentItem(PAGE_FIVE);
                bottom_bar.check(R.id.radio4);
                button0.setTextSize(0);
                button1.setTextSize(0);
                button2.setTextSize(0);
                button3.setTextSize(0);
                button4.setTextSize(15);
                break;
        }
        ToolKits.putInt(this,"fragment",0);
        //bottom_bar.check(R.id.radio0);
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
            int i= ToolKits.fetchInt(this,"fragment",0);
            switch (i)
            {
                case 0:
                    bottom_bar.check(R.id.radio0);
                    break;
                case 1:
                    bottom_bar.check(R.id.radio1);
                    break;
                case 2:
                    bottom_bar.check(R.id.radio2);
                    break;
                case 3:
                    bottom_bar.check(R.id.radio3);
                    break;
                case 4:
                    bottom_bar.check(R.id.radio4);
                    break;

            }
            //bottom_bar.check(R.id.radio0);

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


    /**
     * 通过反射的方式获取状态栏高度
     * @return
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


}
