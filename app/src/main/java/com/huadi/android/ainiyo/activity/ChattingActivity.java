package com.huadi.android.ainiyo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.frag.ChattingFragment;

/**
 * Created by zhidong on 2017/7/31.
 */

public class ChattingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        /*actionBar.setTitle(mChatId);*/
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        actionBar.setTitle(name);
        int picture = intent.getIntExtra("img",0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null){
            fragment = ChattingFragment.newInstance(name,picture);
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }*/
}
