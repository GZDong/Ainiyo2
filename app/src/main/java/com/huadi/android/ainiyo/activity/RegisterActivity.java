package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.util.SignInUtil;
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

public class RegisterActivity extends AppCompatActivity {

    @ViewInject(R.id.register2)
    private Button register2;
    @ViewInject(R.id.register_name)
    private EditText register_name;
    @ViewInject(R.id.register_phone)
    private EditText register_phone;
    @ViewInject(R.id.register_pwd1)
    private EditText register_pwd1;
    @ViewInject(R.id.register_pwd2)
    private EditText register_pwd2;
    @ViewInject(R.id.check_box)
    private CheckBox check_box;
    @ViewInject(R.id.back)
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.theme_statusBar_red));
        }
        ViewUtils.inject(this);
    }

    @OnClick({R.id.register2,R.id.back})
    public void onClick (View v){
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
            case R.id.register2:
                if(register_name.getText().toString().trim().length()<=0){
                    register_name.setError("用户名不能为空！");
                    return;
                }



                if(register_phone.getText().toString().trim().length()<=0){
                    register_phone.setError("电话号码不能为空！");
                    return;
                }
                if (register_phone.getText().toString().trim().length() != 11) {
                    register_phone.setError("错误的电话号码！");
                    return;
                }
                if(register_pwd1.getText().toString().trim().length()<=0){
                    register_pwd1.setError("密码不能为空！");
                    return;
                }
                if(register_pwd1.getText().toString().trim().length()>7&&register_pwd1.getText().toString().trim().length()<17){
                    register_pwd1.setError("密码长度不能小于6");
                    return;
                }
                if(!register_pwd1.getText().toString().equals(register_pwd2.getText().toString())){
                    register_pwd2.setError("两次输入的密码不相同！");
                    return;
                }
                if(!check_box.isChecked()){
                    Toast.makeText(RegisterActivity.this, "没有接受用户条例，注册失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestParams params=new RequestParams();
                params.addBodyParameter("name",register_name.getText().toString());
                params.addBodyParameter("phone",register_phone.getText().toString());
                params.addBodyParameter("pwd",register_pwd1.getText().toString());
                HttpUtils http=new HttpUtils();
                http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/register",params,new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInFo) {
                        String info = responseInFo.result.toString();
                        try {
                            JSONObject object = new JSONObject(info);
                            String msg = object.getString("Msg");
                            if (msg.equals("success")) {

                                //如果自己的服务器注册成功，就拿数据去环形服务器注册
                                SignInUtil.signUp(RegisterActivity.this, register_name.getText().toString(), register_pwd1.getText().toString());

                                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(RegisterActivity.this, "注册失败，请重试！", Toast.LENGTH_SHORT).show();
                    }

                });
        }
    }
}