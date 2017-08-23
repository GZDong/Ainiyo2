package com.huadi.android.ainiyo.frag;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;

import static com.huadi.android.ainiyo.R.id.textView;

/**
 * Created by zhidong on 2017/8/22.
 */

public class HobbyFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.text_hobby, null, false);
        TextView textView = (TextView) view;

        return new AlertDialog.Builder(getActivity())
                .setView(textView)
                .create();
    }
}
