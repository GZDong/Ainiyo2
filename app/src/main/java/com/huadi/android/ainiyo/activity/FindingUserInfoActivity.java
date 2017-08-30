package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_getuserinfo_byName_Interface;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.FindingInfo;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.frag.FlagFragment;
import com.huadi.android.ainiyo.gson.ResultForUserInfo;
import com.huadi.android.ainiyo.util.DateUtil;
import com.huadi.android.ainiyo.util.ImgScaleUtil;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FindingUserInfoActivity extends AppCompatActivity {

    private ListView mListView;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private FindingInfo fi;

    private Toolbar mToolbar;

    private TextView mTextView;

    private TextView sexAndageText;
    private TextView signText;
    private TextView areaText;
    private ImageView sexImage;
    private ImageView personImage;

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
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.gray));
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
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        sexAndageText = (TextView) findViewById(R.id.sex_and_age);
        sexImage = (ImageView) findViewById(R.id.sex_image);
        signText = (TextView) findViewById(R.id.signField);
        areaText = (TextView) findViewById(R.id.area_text);
        personImage = (ImageView) findViewById(R.id.person_image);

        //这里做请求数据，只在这里用的，不储存进数据库，所以就不单独用类包装了
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.24.168.102:8080/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostRequest_getuserinfo_byName_Interface getuserinfo_byName_interface = retrofit.create(PostRequest_getuserinfo_byName_Interface.class);
        Observable<ResultForUserInfo> observable = getuserinfo_byName_interface.getObservable(((ECApplication)getApplication()).sessionId,fi.getName());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultForUserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultForUserInfo resultForUserInfo) {
                        if (resultForUserInfo.getStatus().equals("0")){
                            if (resultForUserInfo.getResult().getAreaName()!= null) {
                                areaText.setText(resultForUserInfo.getResult().getAreaName());
                            }else {
                                areaText.setText("-");
                            }
                            if (!TextUtils.isEmpty(resultForUserInfo.getResult().getAutograph())){
                                signText.setText(resultForUserInfo.getResult().getAutograph());
                            } else {
                                signText.setTextColor(getResources().getColor(R.color.little_gray));
                                signText.setText("该好友还没有设置签名");
                            }
                            String sex = null;
                            String age = null;

                            if (resultForUserInfo.getResult().getGentle()!=null) {
                                if (resultForUserInfo.getResult().getGentle().equals("0")) {
                                    sex = "男";
                                    sexImage.setImageResource(R.drawable.boy2);
                                } else if (resultForUserInfo.getResult().getGentle().equals("1")) {
                                    sex = "男";
                                    sexImage.setImageResource(R.drawable.boy2);
                                } else if (resultForUserInfo.getResult().getGentle().equals("2")) {
                                    sex = "女";
                                    sexImage.setImageResource(R.drawable.girl2);
                                }
                            }else {
                                sex = "女";
                                sexImage.setImageResource(R.drawable.girl2);
                            }
                            if (resultForUserInfo.getResult().getBirthday()!=null){
                                String subStr = resultForUserInfo.getResult().getBirthday().substring(0,4);
                                int diff = Integer.valueOf(DateUtil.getYear())-Integer.valueOf(subStr);
                                age = String.valueOf(diff);
                            }else {
                                age = "-";
                            }
                            sexAndageText.setText(sex + " " + age);

                            if (!TextUtils.isEmpty(resultForUserInfo.getResult().getAvatar())){
                                Glide.with(FindingUserInfoActivity.this).load(resultForUserInfo.getResult().getAvatar()).into(personImage);
                            }else {
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.girl4);
                                personImage.setImageBitmap(bitmap);
                            }
                        }

                    }
                });

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

    @OnClick(R.id.btn_finding_add_friend)
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
