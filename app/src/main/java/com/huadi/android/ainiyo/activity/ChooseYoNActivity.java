package com.huadi.android.ainiyo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_getrequesst_Interface;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_getuserinfo_byName_Interface;
import com.huadi.android.ainiyo.Retrofit2.PostRequset_agreerequest_Interface;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.Friends;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.gson.ResultForAgree;
import com.huadi.android.ainiyo.gson.ResultForRqstList;
import com.huadi.android.ainiyo.gson.ResultForUserInfo;
import com.huadi.android.ainiyo.util.DateUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhidong on 2017/8/14.
 */

public class ChooseYoNActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "test";

    private TextView reasonText;
    private Button acceptBtn;
    private Button refuseBtn;
    private Toolbar mToolbar;

    private CollapsingToolbarLayout mCoordinatorLayout;
    private ImageView mPersonImg;
    private TextView mSexAndAge;
    private ImageView mSexImage;
    private TextView mAreaText;

    private String name;
    private String reason;

    private String rqstId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseyon);
        // setContentView(R.layout.activity_new_friend_invitation);
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

        Log.e("name","用户："+ name+" 请求添加你为好友，理由是：" + reason);


        reasonText = (TextView) findViewById(R.id.reason);
        acceptBtn = (Button) findViewById(R.id.accept_btn);
        refuseBtn = (Button) findViewById(R.id.refuse_btn);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mPersonImg = (ImageView) findViewById(R.id.person_image);
        mSexAndAge = (TextView) findViewById(R.id.sex_and_age);
        mSexImage = (ImageView) findViewById(R.id.sex_image);
        mAreaText = (TextView) findViewById(R.id.area_text);

        mCoordinatorLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCoordinatorLayout.setExpandedTitleColor(getResources().getColor(R.color.gray));
        mCoordinatorLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        mCoordinatorLayout.setTitle(name);

        //这里做请求数据，只在这里用的，不储存进数据库，所以就不单独用类包装了
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.24.168.102:8080/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostRequest_getuserinfo_byName_Interface getuserinfo_byName_interface = retrofit.create(PostRequest_getuserinfo_byName_Interface.class);
        Observable<ResultForUserInfo> observable = getuserinfo_byName_interface.getObservable(((ECApplication) getApplication()).sessionId, name);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultForUserInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: 完成好友信息请求");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: 好友信息请求失败");
                    }

                    @Override
                    public void onNext(ResultForUserInfo resultForUserInfo) {
                        Log.e(TAG, "onNext: 请求正常");
                        if (resultForUserInfo.getStatus().equals("0")) {
                            Log.e(TAG, "onNext: 请求成功");
                            Log.e(TAG, "onNext: " + resultForUserInfo.getResult().getAreaName());
                            if (!TextUtils.isEmpty(resultForUserInfo.getResult().getAreaName())) {
                                mAreaText.setText(resultForUserInfo.getResult().getAreaName());
                            } else {
                                mAreaText.setText("未知");
                            }

                            String sex = null;
                            String age = null;

                            Log.e(TAG, "onNext: " + resultForUserInfo.getResult().getGentle());
                            if (!TextUtils.isEmpty(resultForUserInfo.getResult().getGentle())) {
                                if (resultForUserInfo.getResult().getGentle().equals("0")) {
                                    sex = "男";
                                    mSexImage.setImageResource(R.drawable.boy2);
                                } else if (resultForUserInfo.getResult().getGentle().equals("1")) {
                                    sex = "男";
                                    mSexImage.setImageResource(R.drawable.boy2);
                                } else if (resultForUserInfo.getResult().getGentle().equals("2")) {
                                    sex = "女";
                                    mSexImage.setImageResource(R.drawable.girl2);
                                }
                            } else {
                                sex = "女";
                                mSexImage.setImageResource(R.drawable.girl2);
                            }
                            Log.e(TAG, "onNext: " + resultForUserInfo.getResult().getBirthday());
                            if (!TextUtils.isEmpty(resultForUserInfo.getResult().getBirthday())) {
                                String subStr = resultForUserInfo.getResult().getBirthday().substring(0, 4);
                                int diff = Integer.valueOf(DateUtil.getYear()) - Integer.valueOf(subStr);
                                age = String.valueOf(diff);
                            } else {
                                age = "-";
                            }
                            mSexAndAge.setText(sex + " " + age);

                            Log.e(TAG, "onNext: " + resultForUserInfo.getResult().getAvatar());
                            if (!TextUtils.isEmpty(resultForUserInfo.getResult().getAvatar())) {
                                Glide.with(ChooseYoNActivity.this).load(resultForUserInfo.getResult().getAvatar()).into(mPersonImg);
                            } else {
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
                                mPersonImg.setImageBitmap(bitmap);
                            }
                        } else {
                            mAreaText.setText("未知");
                            mSexAndAge.setText("女" + " " + "-");
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl4);
                            mPersonImg.setImageBitmap(bitmap);
                        }
                    }
                });

        setSupportActionBar(mToolbar);

        if (!TextUtils.isEmpty(reason)){
            reasonText.setText(name + "  :   " + reason);
        }else {
            reasonText.setText(name + "  :  ");
        }


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
                final ECApplication ecApplication = (ECApplication) getApplication();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://120.24.168.102:8080/")
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PostRequest_getrequesst_Interface getrequesstInterface = retrofit.create(PostRequest_getrequesst_Interface.class);
                Observable<ResultForRqstList> observable = getrequesstInterface.getObservable(ecApplication.sessionId,"1","10");
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultForRqstList>() {
                            @Override
                            public void onCompleted() {
                                Log.e("test", "onCompleted___请求申请表完成");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("test", "onError___请求申请表异常");
                            }

                            @Override
                            public void onNext(ResultForRqstList resultForRqstList) {
                                Log.e("test", "onNext___请求申请表");
                                if (resultForRqstList.getStatus().equals("330")) {
                                    Log.e("test", "onNext___申请表返回成功");
                                    if (resultForRqstList.getResult() != null) {
                                        Log.e("test", "申请表有值");
                                        //这一步应该是必执行的
                                        rqstId = resultForRqstList.getResult().getData().get(0).getUserid();
                                        Log.e("test", "申请者的id是：" + rqstId);
                                        //获得申请人ID后进行同意
                                        Retrofit ret = new Retrofit.Builder()
                                                .baseUrl("http://120.24.168.102:8080/")
                                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        PostRequset_agreerequest_Interface agreerequestInterface = ret.create(PostRequset_agreerequest_Interface.class);
                                        Log.e("test", "在添加好友时，传递过去的参数：" + ecApplication.sessionId + " " + rqstId + " ");
                                        Observable<ResultForAgree> obser = agreerequestInterface.getObservable(ecApplication.sessionId, rqstId, "1");
                                        obser.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Subscriber<ResultForAgree>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        Log.e("test", "onCompleted___同意好友完成");
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.e("test", "onError___同意好友异常");
                                                    }

                                                    @Override
                                                    public void onNext(ResultForAgree resultForAgree) {
                                                        Log.e("test", "onNext___同意好友");
                                                        if (resultForAgree.getStatus().equals("330")) {
                                                            Log.e("test", "onNext___好友同意成功了");
                                                            //在这里重新请求好友列表，并且刷新
                                                            FriendsLab.get(ChooseYoNActivity.this).reRequsetFriList();
                                                        } else {
                                                            Log.e("test", "onNext__返回的status值不对，是：" + resultForAgree.getStatus());
                                                        }
                                                    }
                                                });
                                        //使用第三方同意，目的是使对方能监听到
                                        Observable.create(new Observable.OnSubscribe<String>() {
                                            @Override
                                            public void call(Subscriber<? super String> subscriber) {
                                                try {
                                                    EMClient.getInstance().contactManager().acceptInvitation(name);
                                                } catch (HyphenateException e) {
                                                    e.printStackTrace();
                                                }
                                                /*Friends friends = new Friends(UserInfoLab.get(ChooseYoNActivity.this).getUserInfo().getUsername(), name);
                                                FriendsLab.get(ChooseYoNActivity.this, UserInfoLab.get(ChooseYoNActivity.this).getUserInfo()).addFriend(friends);*/
                                                subscriber.onNext(name);
                                            }
                                        }).subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Subscriber<String>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        Log.e("test", "onCompleted:");
                                                        Log.e("test", "执行一次Async完毕在监听器处");
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.e("test", "执行异常");
                                                    }

                                                    @Override
                                                    public void onNext(String s) {
                                                        Log.e("test", "接受好友 " + s);
                                                        Toast.makeText(ChooseYoNActivity.this, "接受好友 " + s, Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                });
                                    } else {
                                        Log.e("test", "申请表为空");
                                        rqstId = null;
                                    }
                                }else{
                                    Log.e("test","返回的状态码是："+ resultForRqstList.getStatus());
                                }
                            }
                        });


                break;
            case R.id.refuse_btn:
                //服务器端
                final ECApplication application = (ECApplication) getApplication();
                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl("http://120.24.168.102:8080/")
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PostRequest_getrequesst_Interface getreqInterface = retrofit2.create(PostRequest_getrequesst_Interface.class);
                Observable<ResultForRqstList> obs = getreqInterface.getObservable(application.sessionId,"1","10");
                obs.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultForRqstList>() {
                            @Override
                            public void onCompleted() {
                                Log.e("test", "onCompleted___请求申请表完成");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("test", "onError___请求申请表异常");
                            }

                            @Override
                            public void onNext(ResultForRqstList resultForRqstList) {
                                Log.e("test", "onNext___请求申请表");
                                if (resultForRqstList.getStatus().equals("330")) {
                                    Log.e("test", "onNext___申请表返回成功");
                                    if (resultForRqstList.getResult() != null) {
                                        Log.e("test", "申请表有值");
                                        //这一步应该是必执行的
                                        rqstId = resultForRqstList.getResult().getData().get(0).getUserid();
                                        //获得申请人ID后进行同意
                                        Retrofit ret2 = new Retrofit.Builder()
                                                .baseUrl("http://120.24.168.102:8080/")
                                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        PostRequset_agreerequest_Interface requestInterface = ret2.create(PostRequset_agreerequest_Interface.class);

                                        Observable<ResultForAgree> obser3 = requestInterface.getObservable(application.sessionId, rqstId, "2");
                                        obser3.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Subscriber<ResultForAgree>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        Log.e("test", "onCompleted___拒绝好友完成");
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.e("test", "onError___拒绝好友异常");
                                                    }

                                                    @Override
                                                    public void onNext(ResultForAgree resultForAgree) {
                                                        Log.e("test", "onNext___拒绝好友");
                                                        if (resultForAgree.getStatus().equals("332")) {
                                                            Log.e("test", "onNext___好友拒绝成功了");
                                                        } else {
                                                            Log.e("test", "onNext__返回的status值不对，是：" + resultForAgree.getStatus());
                                                        }
                                                    }
                                                });
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
                                                        Log.e("test", "onCompleted:");
                                                        Log.e("test", "执行一次Async完毕在监听器处");
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.e("test", "执行异常");
                                                    }

                                                    @Override
                                                    public void onNext(String s) {
                                                        Log.e("test", "拒绝好友 " + s);
                                                        Toast.makeText(ChooseYoNActivity.this, "拒绝好友 " + s, Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                });
                                        Log.e("test", "申请者的id是：" + rqstId);
                                    } else {
                                        Log.e("test", "申请表为空");
                                        rqstId = null;
                                    }
                                }
                            }
                        });


                break;
        }
    }
}
