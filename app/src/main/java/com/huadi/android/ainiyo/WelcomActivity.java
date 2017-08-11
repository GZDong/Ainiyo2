package com.huadi.android.ainiyo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.huadi.android.ainiyo.activity.LoginActivity;

public class WelcomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        final Boolean islogin=pref.getBoolean("islogin",false);
        new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg){
                if(!islogin){
                startActivity(new Intent(WelcomActivity.this, LoginActivity.class));
                finish();}
                if(islogin){
                    startActivity(new Intent(WelcomActivity.this,MainActivity.class));
                    finish();
                }
                return true;
            }
        }).sendEmptyMessageDelayed(0,2500);
    }
}
