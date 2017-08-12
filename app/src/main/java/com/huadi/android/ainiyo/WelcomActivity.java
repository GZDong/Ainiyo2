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
import com.huadi.android.ainiyo.activity.LoadingDialog;
import com.huadi.android.ainiyo.activity.LoginActivity;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.huadi.android.ainiyo.gson.ResultForLogin;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WelcomActivity extends AppCompatActivity {

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

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

        new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg){
                if(!islogin){
                startActivity(new Intent(WelcomActivity.this, LoginActivity.class));
                finish();}
                if(islogin){
                    SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                    username=pref.getString("name","");
                    password=pref.getString("pwd","");
                    RequestParams params=new RequestParams();
                    params.addBodyParameter("name",username);
                    params.addBodyParameter("pwd",password);

                    //初始化用户信息
                    UserInfo userInfo = new UserInfo(username,password,R.drawable.right_image);
                    UserInfoLab.get(WelcomActivity.this,userInfo);

                    HttpUtils http=new HttpUtils();
                    http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/login",params,new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInFo){
                                    String info=responseInFo.result.toString();
                                    try{
                                        JSONObject object=new JSONObject(info);
                                        String msg=object.getString("Msg");

                                        //获得sessionId，保存在Application里作为全局变量
                                        ECApplication application = (ECApplication) getApplication();
                                        application.sessionId = object.getString("Sessionid");

                                        Log.e("test",application.sessionId);

                                        if(msg.equals("success")){
                                            final LoadingDialog dia=new LoadingDialog(WelcomActivity.this);
                                            dia.setMessage("正在登陆中..").show();
                                            new Thread(new Runnable(){
                                                @Override
                                                public void run(){
                                                    try{
                                                        Thread.sleep(2000);
                                                        dia.dismiss();
                                                    }
                                                    catch (InterruptedException e){
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }).start();
                                            startActivity(new Intent(WelcomActivity.this,MainActivity.class));
                                            dia.dismiss();   //***BUG
                                            finish();
                                        }
                                        Toast.makeText(WelcomActivity.this,msg,Toast.LENGTH_SHORT).show();

                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                    }



                                }
                                @Override
                                public void onFailure(HttpException error, String msg){
                                    Toast.makeText(WelcomActivity.this,"登陆失败，请重试！",Toast.LENGTH_SHORT).show();
                                }


                            }
                    );

                    startActivity(new Intent(WelcomActivity.this,MainActivity.class));
                    finish();
                }
                return true;
            }
        }).sendEmptyMessageDelayed(0,2500);
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
}
