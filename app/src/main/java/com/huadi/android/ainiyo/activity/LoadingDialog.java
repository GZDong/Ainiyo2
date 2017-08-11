package com.huadi.android.ainiyo.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;

/**
 * Created by 45990 on 2017/8/8.
 */

public class LoadingDialog extends Dialog {

    private TextView tv_text;

    public LoadingDialog(Context context) {
        super(context);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loading);
        tv_text = (TextView) findViewById(R.id.tv_text);
        setCanceledOnTouchOutside(false);
    }

    public LoadingDialog setMessage(String message) {
        tv_text.setText(message);
        return this;
    }
}
