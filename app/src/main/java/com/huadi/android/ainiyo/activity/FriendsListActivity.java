package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;

import com.huadi.android.ainiyo.adapter.SortAdapter;
import com.huadi.android.ainiyo.adapter.SortAdapter.ViewHolder;
import com.huadi.android.ainiyo.definedView.ClearEditText;
import com.huadi.android.ainiyo.definedView.SideBar;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;

import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.util.PinyinComparator;
import com.huadi.android.ainiyo.util.PinyinUtils;
import com.huadi.android.ainiyo.util.ToolKits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhidong on 2017/8/4.
 */

public class FriendsListActivity extends AppCompatActivity{

    private Toolbar mToolbar;

    private List<Friends> FriList;
    private List<Friends> SourceDateList;

    private RecyclerView mRecyclerView;

    private SideBar sideBar;

    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;

    private UserInfo mUserInfo;

    LinearLayoutManager manager;

    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fri_list);

        Intent intent = getIntent();
        mUserInfo = (UserInfo) intent.getSerializableExtra("userInfo");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }

        initViews();


        mToolbar = (Toolbar) findViewById(R.id.toolbar_contact);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle(R.string.contact_title);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] names = FriendsLab.get(this, mUserInfo).getNames();
        SourceDateList = filledData(names);
        adapter = new SortAdapter(this, SourceDateList, mUserInfo);
        mRecyclerView.setAdapter(adapter);
    }

    private void initViews() {


        FriList = new ArrayList<>();
        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sideBar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }

            }
        });
        SourceDateList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        String[] names = FriendsLab.get(this, mUserInfo).getNames();

        SourceDateList = filledData(names);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        //RecyclerView社置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new SortAdapter(this, SourceDateList, mUserInfo);
        mRecyclerView.setAdapter(adapter);
        //item点击事件
        /*adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, ((Friends)adapter.getItem(position)).getName(),Toast.LENGTH_SHORT).show();
            }
        });*/
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ToolKits.putInt(this,"fragment",1);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.add_friend:
                Intent intent1 = new Intent(this, AddFriendActivity.class);
                startActivity(intent1);
                return true;
            case R.id.delete_friend:
                //开启新的界面
                Intent intent2 = new Intent(this,DeleteFriendActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */
    private List<Friends> filledData(String[] date) {
        List<Friends> mSortList = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            Friends sortModel = FriendsLab.get(this, mUserInfo).getFriend(date[i]);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setLetters(sortString.toUpperCase());
            } else {
                sortModel.setLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Friends> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (Friends sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);
    }

}
