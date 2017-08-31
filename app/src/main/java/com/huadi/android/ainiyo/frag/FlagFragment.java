package com.huadi.android.ainiyo.frag;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.entity.FriendsLab;

/**
 * Created by zhidong on 2017/8/22.
 */

public class FlagFragment extends DialogFragment {

    public static FlagFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        FlagFragment fragment = new FlagFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String name = getArguments().getString("name");
        final EditText editText = (EditText) LayoutInflater.from(getActivity()).inflate(R.layout.text_flag, null, false);
        if (!TextUtils.isEmpty(FriendsLab.get(getActivity()).getFriend(name).getTagMsg())){
            editText.setText(FriendsLab.get(getActivity()).getFriend(name).getTagMsg());
        }else {
            editText.setText(name);
        }
        editText.setSelection(editText.getText().length());
        return new AlertDialog.Builder(getActivity())
                .setView(editText)
                .setTitle("设置备和标签")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(editText.getText())) {
                            Toast.makeText(getActivity(), "不能为空！", Toast.LENGTH_LONG).show();
                        }else {
                            if (editText.getText().toString().equals(name)){
                                FriendsLab.get(getActivity()).setFriTag(name,"");
                                Intent intent = new Intent("com.huadi.android.ainiyo.refreshName");
                                intent.putExtra("NewName",name);
                                getActivity().sendBroadcast(intent);
                            }else{
                                FriendsLab.get(getActivity()).setFriTag(name,editText.getText().toString());
                                Intent intent = new Intent("com.huadi.android.ainiyo.refreshName");
                                intent.putExtra("NewName",editText.getText().toString());
                                getActivity().sendBroadcast(intent);
                            }
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }
}
