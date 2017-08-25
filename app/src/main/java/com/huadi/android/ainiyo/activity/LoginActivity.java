package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.FriendsLab;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.entity.UserInfoLab;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity  {

    @ViewInject(R.id.register1)
    private TextView register1;
    @ViewInject(R.id.Login2)
    private Button Login2;
    @ViewInject(R.id.login_name)
    private EditText login_name;
    @ViewInject(R.id.login_pwd)
    private EditText login_pwd;
    @ViewInject(R.id.check_box)
    private CheckBox check_box;

    private UserInfo mUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        ActionBar actionbar = getSupportActionBar();

        Login2.getBackground().setAlpha(200);

        //调整状态栏(工具栏上方本来是灰色的，现在统一）的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    localLayoutParams.flags);
        }

        if (actionbar != null) {
            actionbar.hide();
        }
        ViewUtils.inject(this);
            SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
            String username=pref.getString("username","");
            String password=pref.getString("password","");
            Boolean isremember=pref.getBoolean("remember_pwd",false);
        if(isremember){
            login_name.setText(username);
            login_pwd.setText(password);
        check_box.setChecked(true);}


    }



    @OnClick({R.id.register1,R.id.Login2})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.register1:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.Login2:
                if(login_name.getText().toString().trim().length()<=0){
                    login_name.setError("用户名不能为空！");
                    return;
                }
                if(login_pwd.getText().toString().trim().length()<=0){
                    login_pwd.setError("密码不能为空！");
                    return;
                }
                RequestParams params=new RequestParams();
                params.addBodyParameter("name",login_name.getText().toString());
                params.addBodyParameter("pwd",login_pwd.getText().toString());
                //初始化用户信息
                final UserInfo userInfo = new UserInfo(login_name.getText().toString(), login_pwd.getText().toString());


               // Log.e("test","onLoginActivity "+userInfo.getUsername()+UserInfoLab.get(LoginActivity.this).getUserInfo().getUsername());
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
                                    application.sessionId = null;
                                    application.sessionId = object.getString("Sessionid");
                                    UserInfoLab.get(LoginActivity.this,userInfo).clearUserInfo();
                                    UserInfoLab.get(LoginActivity.this,userInfo);
                                    FriendsLab.get(LoginActivity.this, userInfo).setFriListNull();
                                    FriendsLab.get(LoginActivity.this, userInfo).initFriends();

                                    Log.e("test",application.sessionId);

                                    if(msg.equals("success")){

                                        if(check_box.isChecked()){
                                            SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                                            editor.putString("username",login_name.getText().toString());
                                            editor.putString("password",login_pwd.getText().toString());
                                            editor.putBoolean("remember_pwd",true);
                                            editor.apply();
                                        }
                                        if(!check_box.isChecked()){
                                            SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                                            editor.remove("username");
                                            editor.remove("password");
                                            editor.putBoolean("remember_pwd",false);
                                            editor.apply();
                                        }
                                     SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                                        editor.putString("name",login_name.getText().toString());
                                        editor.putString("pwd",login_pwd.getText().toString());
                                        editor.putBoolean("islogin",true);
                                        editor.apply();

                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        finish();
                                    }
                                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();

                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(HttpException error,String msg){
                                Toast.makeText(LoginActivity.this,"登陆失败，请重试！",Toast.LENGTH_SHORT).show();
                            }


                        }
                );
        }
    }
}