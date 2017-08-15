package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.Retrofit2.GetRequest_checkName_Interface;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_ReqFri_Interface;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.definedView.ClearEditText;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.gson.ResultForCheckName;
import com.huadi.android.ainiyo.gson.ResultForRequset;
import com.huadi.android.ainiyo.util.SignInUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhidong on 2017/8/10.
 */

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar ;

    private ClearEditText mClearEditText;
    private Button mSearchBtn;
    private EditText mEditText;

    private String attach;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }

        if (!EMClient.getInstance().isLoggedInBefore()) {
            Toast.makeText(AddFriendActivity.this,"第三方没有登陆",Toast.LENGTH_LONG).show();
            UserInfo userInfo = UserInfoLab.get(AddFriendActivity.this).getUserInfo();
            String name = userInfo.getUsername();
            String pass = userInfo.getPassword();
            SignInUtil.signIn(name,pass,AddFriendActivity.this);
        }else {
            Toast.makeText(AddFriendActivity.this,"第三方登陆",Toast.LENGTH_LONG).show();
            Toast.makeText(AddFriendActivity.this, EMClient.getInstance().getCurrentUser(),Toast.LENGTH_LONG).show();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_add_friend);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle(R.string.add_friend);
        }
        mSearchBtn = (Button) findViewById(R.id.search_btn) ;
        mClearEditText = (ClearEditText) findViewById(R.id.search_edit);
        mEditText = (EditText) findViewById(R.id.attach_text);
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSearchBtn.setVisibility(View.VISIBLE);
                mEditText.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSearchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_btn){

            /*if (!TextUtils.isEmpty(mClearEditText.getText())) {
                ECApplication ecApplication =(ECApplication) getApplication();

                *//* RequestParams params=new RequestParams();
                params.addBodyParameter("sessionid",ecApplication.sessionId);
                params.addBodyParameter("friendid",mClearEditText.getText().toString());
                params.addBodyParameter("attach",mEditText.getText().toString());

                HttpUtils http=new HttpUtils();
                http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/addfriend",params,new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInFo){
                                String info=responseInFo.result.toString();
                                try{
                                    JSONObject object=new JSONObject(info);
                                    String msg=object.getString("Msg");
                                    int Status = object.getInt("Status");
                                    switch (Status){
                                        case 310:
                                            Toast.makeText(AddFriendActivity.this,"添加成功!",Toast.LENGTH_LONG).show();
                                            // FriendsLab.get(AddFriendActivity.this, UserInfoLab.get(AddFriendActivity.this).getUserInfo()).addFriend(response.body().);
                                            break;
                                        case 201:
                                            Toast.makeText(AddFriendActivity.this,"请先登陆!",Toast.LENGTH_LONG).show();
                                            break;
                                        case 93:
                                            Toast.makeText(AddFriendActivity.this,"服务器发生错误!",Toast.LENGTH_LONG).show();
                                            break;
                                        case 97:
                                            Toast.makeText(AddFriendActivity.this,"输入了非法符号，请重新输入!",Toast.LENGTH_LONG).show();
                                            break;
                                        case 181:
                                            Toast.makeText(AddFriendActivity.this,"该用户不存在，请确认!",Toast.LENGTH_LONG).show();
                                            break;
                                        case 312:
                                            Toast.makeText(AddFriendActivity.this,"已经发送好友请求!",Toast.LENGTH_LONG).show();
                                            break;
                                        case 380:
                                            Toast.makeText(AddFriendActivity.this,"你们已经是好友了!",Toast.LENGTH_LONG).show();
                                            break;
                                        case 99:
                                            Toast.makeText(AddFriendActivity.this,"发生未知错误!",Toast.LENGTH_LONG).show();
                                            break;
                                    }


                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }



                            }
                            @Override
                            public void onFailure(HttpException error, String msg){

                            }


                        }
                );*//*
                Retrofit retrofit = new  Retrofit.Builder()
                                         .baseUrl("http://120.24.168.102:8080/")
                                         .addConverterFactory(GsonConverterFactory.create())
                                         .build();
                PostRequest_ReqFri_Interface reqFriInterface = retrofit.create(PostRequest_ReqFri_Interface.class);
                Call<ResultForRequset> call = null;
                if (!TextUtils.isEmpty(mEditText.getText())) {
                    call = reqFriInterface.getCall(ecApplication.sessionId,mClearEditText.getText().toString(),mEditText.getText().toString());
                }else{
                    call = reqFriInterface.getCall(ecApplication.sessionId,mClearEditText.getText().toString()," ");
                }
                Log.e("testCanshu",ecApplication.sessionId +mClearEditText.getText().toString() + mEditText.getText().toString());
                call.enqueue(new Callback<ResultForRequset>() {
                    @Override
                    public void onResponse(Call<ResultForRequset> call, Response<ResultForRequset> response) {
                        if (response.isSuccessful()) {
                            switch (response.body().getStatus()){
                                case 310:
                                    Toast.makeText(AddFriendActivity.this,"添加成功!",Toast.LENGTH_LONG).show();
                                   // FriendsLab.get(AddFriendActivity.this, UserInfoLab.get(AddFriendActivity.this).getUserInfo()).addFriend(response.body().);
                                    break;
                                case 201:
                                    Toast.makeText(AddFriendActivity.this,"请先登陆!",Toast.LENGTH_LONG).show();
                                    break;
                                case 93:
                                    Toast.makeText(AddFriendActivity.this,"服务器发生错误!",Toast.LENGTH_LONG).show();
                                    break;
                                case 97:
                                    Toast.makeText(AddFriendActivity.this,"输入了非法符号，请重新输入!",Toast.LENGTH_LONG).show();
                                    break;
                                case 181:
                                    Toast.makeText(AddFriendActivity.this,"该用户不存在，请确认!",Toast.LENGTH_LONG).show();
                                    break;
                                case 312:
                                    Toast.makeText(AddFriendActivity.this,"已经发送好友请求!",Toast.LENGTH_LONG).show();
                                    break;
                                case 380:
                                    Toast.makeText(AddFriendActivity.this,"你们已经是好友了!",Toast.LENGTH_LONG).show();
                                    break;
                                case 99:
                                    Toast.makeText(AddFriendActivity.this,"发生未知错误!",Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }else{
                            Toast.makeText(AddFriendActivity.this,"好友请求失败!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultForRequset> call, Throwable t) {
                        Toast.makeText(AddFriendActivity.this,"请检查网络情况!",Toast.LENGTH_LONG).show();
                    }
                });
            }*/
            if (!TextUtils.isEmpty(mClearEditText.getText().toString())) {
                ECApplication ecApplication =(ECApplication) getApplication();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://120.24.168.102:8080/")
                                                          .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                          .build();
                GetRequest_checkName_Interface checkNameInterface = retrofit.create(GetRequest_checkName_Interface.class);
                Observable<ResultForCheckName> call = checkNameInterface.getCall(ecApplication.sessionId,mClearEditText.getText().toString());
                call.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultForCheckName>() {
                            @Override
                            public void onCompleted() {
                                Log.e("test","onCompleted,检测用户是否存在结束");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(ResultForCheckName resultForCheckName) {
                                if (resultForCheckName.getStatus().equals("120")){
                                    Log.e("test","onNext" + resultForCheckName.getMsg());
                                    Toast.makeText(AddFriendActivity.this,"用户不存在",Toast.LENGTH_LONG).show();
                                }else if (resultForCheckName.getStatus().equals("123")){
                                    //在这里缺少向服务器发送添加该好友的信息的代码
                                    Log.e("test","onNext" + resultForCheckName.getMsg());

                                    if (!TextUtils.isEmpty(mEditText.getText().toString())){
                                        attach = mEditText.getText().toString();
                                    }else {
                                        attach = " ";
                                    }
                                    //RxJava实现异步：
                                    Observable.create(new Observable.OnSubscribe<String>() {
                                        @Override
                                        public void call(Subscriber<? super String> subscriber) {
                                            try {
                                                EMClient.getInstance().contactManager().addContact(mClearEditText.getText().toString(),attach);
                                                Log.e("test","OnSubscribe"+Thread.currentThread().getId());
                                            } catch (HyphenateException e) {
                                                e.printStackTrace();
                                            }
                                            subscriber.onNext("完成");
                                        }
                                    }).subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<String>() {
                                                @Override
                                                public void onCompleted() {
                                                    Log.e("test","onCompleted\n");
                                                    Log.e("test","执行一次Async完毕");
                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onNext(String s) {
                                                    Log.e("test","onNext " + s + "\n");
                                                    Log.e("test","onNext"+Thread.currentThread().getId());
                                                }
                                            });
                                    Toast.makeText(AddFriendActivity.this,resultForCheckName.getMsg()+"\n"+"添加好友的信息已经发送第三方服务器，不需要发送给自己这边的服务器",Toast.LENGTH_LONG).show();
                                    finish();
                                }else {
                                    Log.e("test","第三种状态码");
                                }
                            }
                        });

                //异步添加
               // new ().execute(mClearEditText.getText().toString(),attach);
            }

        }
    }
}
