package com.huadi.android.ainiyo.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.EditInfoActivity;
import com.huadi.android.ainiyo.activity.LoginActivity;
import com.huadi.android.ainiyo.activity.PhotoActivity;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.util.SignInUtil;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class MeFragment extends Fragment{
    @ViewInject(R.id.behavior)
    private Button behavior;
    @ViewInject(R.id.logoff)
    private Button logoff;
    @ViewInject(R.id.image)
    private Button image;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me,null);
        // Inflate the layout for this fragment
        ViewUtils.inject(this,view);
        return view;
    }
   @OnClick({R.id.behavior,R.id.logoff,R.id.image})
   public void OnClick(View v){
       switch (v.getId()){
           case R.id.behavior:
               startActivityForResult(new Intent(getActivity(), EditInfoActivity.class),1);
               break;
           case R.id.image:
               startActivity(new Intent(getActivity(),PhotoActivity.class));
               break;
           case R.id.logoff:
               AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
               dialog.setTitle("提示");
               dialog.setMessage("是否退出登录？");
                       dialog.setCancelable(false);
               dialog.setPositiveButton("退出",new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog,int which){
                       SharedPreferences.Editor editor=getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
                       editor.putBoolean("islogin",false);
                       editor.apply();
                       SignInUtil.signOut();
                       startActivity(new Intent(getActivity(),LoginActivity.class));
                       Toast.makeText(getActivity(),"你已退出登录",Toast.LENGTH_LONG).show();
                       //这里网络上没有退出！！

                       getActivity().finish();
                   }});
               dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog,int which){
                   }});
               dialog.show();



       }
   }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

}
