package com.huadi.android.ainiyo.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huadi.android.ainiyo.MainActivity;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.activity.EditInfoActivity;
import com.huadi.android.ainiyo.activity.LoginActivity;
import com.huadi.android.ainiyo.activity.PhotoActivity;
import com.huadi.android.ainiyo.activity.VipApply;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.entity.UserData;
import com.huadi.android.ainiyo.entity.UserInfo;
import com.huadi.android.ainiyo.util.SignInUtil;
import com.huadi.android.ainiyo.util.ToolKits;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

public class MeFragment extends Fragment{
    @ViewInject(R.id.info)
    private LinearLayout info;
    @ViewInject(R.id.logoff)
    private LinearLayout logoff;
    @ViewInject(R.id.xiangce)
    private LinearLayout xiangce;
    @ViewInject(R.id.vipapply)
    private LinearLayout vipapply;
    private TextView te;
    private TextView job_text;
    private TextView vip_text;
    private ImageView avatar_imag;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me,null);
        // Inflate the layout for this fragment
        ViewUtils.inject(this,view);
        SharedPreferences pref=getActivity().getSharedPreferences("data",MODE_PRIVATE);
        String username=pref.getString("username","");
        te=(TextView)view.findViewById(R.id.name);
        te.setText(username);//获取用户名//


        RequestParams params=new RequestParams();
        params.addBodyParameter("sessionid",sessionId);
        HttpUtils http=new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, "http://120.24.168.102:8080/getuserinfo",params,new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInFo){
                        String info=responseInFo.result.toString();
                        try{
                            JSONObject object=new JSONObject(info);
                            String msg=object.getString("Msg");
                            Gson gson=new Gson();
                            UserData userData=gson.fromJson(object.getJSONObject("Result").toString(),UserData.class);

                            String job=userData.getJob();
                            Boolean vip=userData.isVip();
                            String image=userData.getAvatar();
                            //如果获取数据成功，则把数据加载到各项
                            if(msg.equals("success"))
                            job_text=(TextView)getActivity().findViewById(R.id.job_text);
                            job_text.setText(job);
                            avatar_imag=(ImageView)getActivity().findViewById(R.id.avatar_imag);
                            Glide.with(getActivity()).load(image).into(avatar_imag);
                            if(vip){
                                vip_text=(TextView)getActivity().findViewById(R.id.vip_text);
                                vip_text.setText("VIP用户");
                            }
                            else {
                                vip_text=(TextView)getActivity().findViewById(R.id.vip_text);
                                vip_text.setText("普通用户");
                            }




                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }



                    }
                    @Override
                    public void onFailure(HttpException error, String msg){

                    }


                }
        );
        return view;
    }
   @OnClick({R.id.info,R.id.logoff,R.id.xiangce,R.id.vipapply})
   public void OnClick(View v){
       switch (v.getId()){
           case R.id.info:
               startActivityForResult(new Intent(getActivity(), EditInfoActivity.class),1);
               break;
           case R.id.xiangce:
               startActivity(new Intent(getActivity(),PhotoActivity.class));
               break;
           case R.id.vipapply:
               startActivity(new Intent(getActivity(),VipApply.class));
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
