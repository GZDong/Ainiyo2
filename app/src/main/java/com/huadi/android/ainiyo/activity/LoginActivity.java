package com.huadi.android.ainiyo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.activity.LoadingDialog;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.application.ECApplication;
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

public class LoginActivity extends AppCompatActivity {

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
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        ViewUtils.inject(this);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String username = pref.getString("username", "");
        String password = pref.getString("password", "");
        Boolean isremember = pref.getBoolean("remember_pwd", false);
        if (isremember) {
            login_name.setText(username);
            login_pwd.setText(password);
            check_box.setChecked(true);
        }


    }


    @OnClick({R.id.register1, R.id.Login2})
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
                HttpUtils http=new HttpUtils();
                Log.e("test","onSuccessLastStep");
                http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/login",params,new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInFo){
                                Log.e("test","onSuccess");
                                String info=responseInFo.result.toString();
                                try{
                                    JSONObject object=new JSONObject(info);
                                    String msg=object.getString("Msg");

                                    //获得sessionId，保存在Application里作为全局变量
                                    ECApplication application = (ECApplication) getApplication();
                                    application.sessionId = object.getString("Sessionid");

                                    if(msg.equals("success")){
                                        final LoadingDialog dia = new LoadingDialog(LoginActivity.this);
                                        dia.setMessage("正在登陆中..").show();
                                        if (check_box.isChecked()) {
                                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                            editor.putString("username", login_name.getText().toString());
                                            editor.putString("password", login_pwd.getText().toString());
                                            editor.putBoolean("remember_pwd", true);
                                            editor.apply();
                                        }
                                        if (!check_box.isChecked()) {
                                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                            editor.remove("username");
                                            editor.remove("password");
                                            editor.putBoolean("remember_pwd", false);
                                            editor.apply();
                                        }
                                        mUserInfo= new UserInfo(login_name.getText().toString(), login_pwd.getText().toString(), R.drawable.left_image);
                                        //初始化单例，如果数据库里没有过这个用户，就存进数据库，否则直接根据用户输入的
                                        //账号密码来初始化单例
                                        UserInfoLab.get(LoginActivity.this, mUserInfo);

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(2000);
                                                    dia.dismiss();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }).start();
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    if (!msg.equals("success")) {
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //不写东西，按下返回键就没操作
        }
        return false;
    }
}