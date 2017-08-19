package com.huadi.android.ainiyo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.huadi.android.ainiyo.Retrofit2.PostRequest_login_Interface;
import com.huadi.android.ainiyo.activity.LoginActivity;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.gson.ResultForLogin;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelcomActivity extends AppCompatActivity {

    private String username;
    private String password;

    private static final int Turn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        Log.e("test", "一闪而过的WelcomeActivity");

        //改成在开始界面请求必要的权限
        if (ContextCompat.checkSelfPermission(WelcomActivity.this,"android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(WelcomActivity.this,new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"},1);
        }



        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        final Boolean islogin=pref.getBoolean("islogin",false);

        //调整状态栏(工具栏上方本来是灰色的，现在统一）的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }

                if(!islogin){
                startActivity(new Intent(WelcomActivity.this, LoginActivity.class));
                finish();}
                if(islogin){
                    SharedPreferences pref2=getSharedPreferences("data",MODE_PRIVATE);
                    username=pref2.getString("name","");
                    password=pref2.getString("pwd","");

                    //初始化用户信息
                    final UserInfo userInfo = new UserInfo(username, password, R.drawable.right_image);
                    UserInfoLab.get(WelcomActivity.this,userInfo);
                    /*FriendsLab.get(WelcomActivity.this,userInfo).setFriListNull();
                    FriendsLab.get(WelcomActivity.this,userInfo).initFriends();*/

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://120.24.168.102:8080/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                    PostRequest_login_Interface login_interface = retrofit.create(PostRequest_login_Interface.class);

                    Observable<ResultForLogin> observable = login_interface.getCall(username,password);
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ResultForLogin>() {
                                @Override
                                public void onCompleted() {
                                    Log.e("test","onCompleted 完成自动登陆");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(WelcomActivity.this,"登陆异常",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNext(ResultForLogin resultForLogin) {
                                    if (resultForLogin.getStatus().equals("100")){
                                        //获得sessionId，保存在Application里作为全局变量
                                        ECApplication application = (ECApplication) getApplication();
                                        application.sessionId = resultForLogin.getSessionid();

                                        Log.e("test","自动重新登陆成功：" + application.sessionId);
                                        Toast.makeText(WelcomActivity.this,"自动登陆成功",Toast.LENGTH_LONG).show();
                                        FriendsLab.get(WelcomActivity.this, userInfo).setFriListNull();
                                        FriendsLab.get(WelcomActivity.this, userInfo).initFriends();
                                        Intent intent = new Intent("com.huadi.android.ainiyo.refresh");
                                        sendBroadcast(intent);
                                        mHandler.sendEmptyMessageDelayed(Turn, 4000);
                                    }else {
                                        Log.e("test",resultForLogin.getStatus().toString());
                                    }
                                }
                            });

                }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Turn:
                    startActivity(new Intent(WelcomActivity.this, MainActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

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
}
