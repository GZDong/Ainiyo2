package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
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
    @ViewInject(R.id.l_name)
    private TextView l_name;
    @ViewInject(R.id.l_pwd)
    private TextView l_pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        ViewUtils.inject(this);


    }
    @OnClick({R.id.register1,R.id.Login2,R.id.login_name,R.id.login_pwd})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.login_pwd:
                l_pwd.setVisibility(View.VISIBLE);
                l_name.setVisibility(View.GONE);
                break;
            case R.id.login_name:
                l_pwd.setVisibility(View.GONE);
                l_name.setVisibility(View.VISIBLE);
                break;
            case R.id.register1:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.Login2:
                l_pwd.setVisibility(View.GONE);
                l_name.setVisibility(View.GONE);
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
                http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/login",params,new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInFo){
                                String info=responseInFo.result.toString();
                                try{
                                    JSONObject object=new JSONObject(info);
                                    String msg=object.getString("Msg");
                                    if(msg.equals("success")){
                                        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    } Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
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