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
                .addEmail("email@email.com","联系我们")
                .addWebsite("http://git.oschina.net/geange_code/Ainiyo","关注Oschina")
                .setDescription("FBI CAUTION\n此软件受美国法律，大概")
                .create();

        setContentView(aboutPage);
    }
}
