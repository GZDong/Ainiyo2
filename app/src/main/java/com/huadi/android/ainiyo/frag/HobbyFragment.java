package com.huadi.android.ainiyo.frag;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.FriendsLab;

import static com.huadi.android.ainiyo.R.id.textView;

/**
 * Created by zhidong on 2017/8/22.
 */

public class HobbyFragment extends DialogFragment {
    public static HobbyFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        HobbyFragment fragment = new HobbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.text_hobby, null, false);
        TextView textView = (TextView) view;
        String name = (String) getArguments().get("name");
        if (!TextUtils.isEmpty(FriendsLab.get(getActivity()).getFriend(name).getHobby())) {
            textView.setText(FriendsLab.get(getActivity()).getFriend(name).getHobby());
        }else {
            textView.setText("该好友没有设置兴趣爱好");
            textView.setTextColor(getResources().getColor(R.color.little_gray));
        }

        return new AlertDialog.Builder(getActivity())
                .setView(textView)
                .create();
    }
}
