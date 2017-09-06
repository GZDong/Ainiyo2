package com.huadi.android.ainiyo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;

/**
 * Created by rxvincent on 2017/8/31.
 */

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .addEmail("35897293849@qq.com","联系我们")
                .addWebsite("http://git.oschina.net/geange_code/Ainiyo","关注Oschina")
                .setDescription("©Ainiyo 2017~ 版权所有\n沟通联系你我，相会成就婚姻")
                .create();

        setContentView(aboutPage);
    }
}
