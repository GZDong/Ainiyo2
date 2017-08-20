package com.huadi.android.ainiyo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huadi.android.ainiyo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnCheckedChange;

public class EditInfoActivity extends AppCompatActivity {
    @ViewInject(R.id.radio_group)
    private RadioGroup radioGroup;
    @ViewInject(R.id.long_line)
    private View long_line;
    @ViewInject(R.id.short_line)
    private View short_line;
    @ViewInject(R.id.base)
    private RadioButton base;
    @ViewInject(R.id.emotion)
    private RadioButton emotion;
    @ViewInject(R.id.info_layout)
    private LinearLayout info_layout;
    @ViewInject(R.id.emotion_layout)
    private LinearLayout emotion_layout;



    private Animation move_to_left,move_to_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
       ViewUtils.inject(this);
        move_to_left= AnimationUtils.loadAnimation(this,R.anim.move_to_left);
        move_to_right= AnimationUtils.loadAnimation(this,R.anim.move_to_right);
    }
    @OnCheckedChange({R.id.radio_group})
    public void onCheckedChanged(RadioGroup group,int checkedId){
        switch (checkedId){
            case R.id.base:
                   short_line.startAnimation(move_to_left);
                info_layout.setVisibility(View.VISIBLE);
                emotion_layout.setVisibility(View.GONE);
                break;
            case R.id.emotion:
                short_line.startAnimation(move_to_right);
                info_layout.setVisibility(View.GONE);
                emotion_layout.setVisibility(View.VISIBLE);
                break;
        }
    }
}
