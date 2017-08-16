package com.huadi.android.ainiyo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhidong on 2017/8/14.
 */

public class ChooseYoNActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView idText;
    private TextView reasonText;
    private Button acceptBtn;
    private Button refuseBtn;
    private Toolbar mToolbar;

    private String name;
    private String reason;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseyon);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }

    }
    private void initView(){
        name = getIntent().getStringExtra("id");
        reason = getIntent().getStringExtra("reason");

        Log.e("name",name + reason + "++++");

        idText = (TextView) findViewById(R.id.person_id_text);
        reasonText = (TextView) findViewById(R.id.reason);
        acceptBtn = (Button) findViewById(R.id.accept_btn);
        refuseBtn = (Button) findViewById(R.id.refuse_btn);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_choose);

        setSupportActionBar(mToolbar);
        idText.setText(name);
        reasonText.setText(reason);

        acceptBtn.setOnClickListener(this);
        refuseBtn.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("好友请求");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.accept_btn:
                //向自己的服务器上传

                //使用第三方同意，目的是使对方能监听到
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(name);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        Friends friends = new Friends(UserInfoLab.get(ChooseYoNActivity.this).getUserInfo().getUsername(),name);
                        FriendsLab.get(ChooseYoNActivity.this,UserInfoLab.get(ChooseYoNActivity.this).getUserInfo()).addFriend(friends);
                        subscriber.onNext(name);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                Log.e("test","onCompleted:");
                                Log.e("test", "执行一次Async完毕在监听器处");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("test","执行异常");
                            }

                            @Override
                            public void onNext(String s) {
                                Log.e("test","接受好友 " +s);
                                Toast.makeText(ChooseYoNActivity.this,"接受好友 " +s,Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

                break;
            case R.id.refuse_btn:
                //使用第三方拒绝添加好友，让信息回去
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            EMClient.getInstance().contactManager().declineInvitation(name);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext(name);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                Log.e("test","onCompleted:");
                                Log.e("test", "执行一次Async完毕在监听器处");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("test","执行异常");
                            }

                            @Override
                            public void onNext(String s) {
                                Log.e("test","拒绝好友 " +s);
                                Toast.makeText(ChooseYoNActivity.this,"拒绝好友 " +s,Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

                break;
        }
    }
}
